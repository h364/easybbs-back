package com.easybbs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.easybbs.constants.Constants;
import com.easybbs.dto.FileUploadDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.mappers.UserInfoMapper;
import com.easybbs.service.UserInfoService;
import com.easybbs.service.UserMessageService;
import com.easybbs.utils.FileUtils;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.stereotype.Service;

import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.ForumCommentMapper;
import com.easybbs.service.ForumCommentService;
import com.easybbs.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 评论 业务接口实现
 */
@Service("forumCommentService")
public class ForumCommentServiceImpl implements ForumCommentService {

    @Resource
    private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;

    @Resource
    private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private FileUtils fileUtils;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ForumComment> findListByParam(ForumCommentQuery param) {
        List<ForumComment> list = forumCommentMapper.selectList(param);
        if (param.getLoadChildren() != null && param.getLoadChildren()) {
            ForumCommentQuery query = new ForumCommentQuery();
            query.setQueryLikeType(param.getQueryLikeType());
            query.setCurrentUserId(param.getCurrentUserId());
            query.setArticleId(param.getArticleId());
            query.setStatus(param.getStatus());
            List<Integer> pCommentIdList = list.stream().map(ForumComment::getCommentId).distinct().collect(Collectors.toList());
            query.setPcommentIdList(pCommentIdList);

            List<ForumComment> subCommentList = forumCommentMapper.selectList(query);
            Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getCommentId));
            list.forEach(item -> {
                item.setChildren(tempMap.get(item.getCommentId()));
            });
        }
        return list;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ForumCommentQuery param) {
        return this.forumCommentMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ForumComment> list = this.findListByParam(param);
        PaginationResultVO<ForumComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ForumComment bean) {
        return this.forumCommentMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ForumComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumCommentMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ForumComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumCommentMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ForumComment bean, ForumCommentQuery param) {
        StringTools.checkParam(param);
        return this.forumCommentMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ForumCommentQuery param) {
        StringTools.checkParam(param);
        return this.forumCommentMapper.deleteByParam(param);
    }

    /**
     * 根据CommentId获取对象
     */
    @Override
    public ForumComment getForumCommentByCommentId(Integer commentId) {
        return this.forumCommentMapper.selectByCommentId(commentId);
    }

    /**
     * 根据CommentId修改
     */
    @Override
    public Integer updateForumCommentByCommentId(ForumComment bean, Integer commentId) {
        return this.forumCommentMapper.updateByCommentId(bean, commentId);
    }

    /**
     * 根据CommentId删除
     */
    @Override
    public Integer deleteForumCommentByCommentId(Integer commentId) {
        return this.forumCommentMapper.deleteByCommentId(commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeTopType(String userId, Integer commentId, Integer topType) {
        CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
        if (topTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumComment forumComment = forumCommentMapper.selectByCommentId(commentId);
        if (forumComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
        if (forumArticle == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!forumArticle.getUserId().equals(userId) || forumComment.getpCommentId() != 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (forumComment.getTopType().equals(topType)) {
            return;
        }
        if (CommentTopTypeEnum.TOP.getType().equals(topType)) {
            forumCommentMapper.updateTopTypeByArticleId(forumArticle.getArticleId());
        }
        ForumComment updateInfo = new ForumComment();
        updateInfo.setTopType(topType);
        forumCommentMapper.updateByCommentId(updateInfo, commentId);
    }

    @Override
    public void postComment(ForumComment forumComment, MultipartFile image) {
        ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
        if (forumArticle == null || !ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())) {
            throw new BusinessException("评论的文章不存在");
        }
        ForumComment pComment = null;
        if (forumComment.getpCommentId() != 0) {
            pComment = forumCommentMapper.selectByCommentId(forumComment.getpCommentId());
            if (pComment == null) {
                throw new BusinessException("回复的评论不存在");
            }
        }
        if (!StringTools.isEmpty(forumComment.getReplyNickName())) {
            UserInfo userInfo = userInfoService.getUserInfoByUserId(forumComment.getReplyUserId());
            if (userInfo == null) {
                throw new BusinessException("回复的用户不存在");
            }
            forumComment.setReplyNickName(userInfo.getNickName());
        }
        forumComment.setPostTime(new Date());
        if (image != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(image, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.COMMENT_IMAGE);
            forumComment.setImgPath(fileUploadDto.getLocalPath());
        }
        Boolean needAudit = SysCacheUtils.getSysSetting().getAuditSetting().getCommentAudit();
        forumComment.setStatus(needAudit ? CommentStatusEnum.NO_AUDIT.getStatus() : CommentStatusEnum.AUDIT.getStatus());

        forumCommentMapper.insert(forumComment);
        if (needAudit) {
            return;
        }
        updateCommentInfo(forumComment, forumArticle, pComment);
    }

    public void updateCommentInfo(ForumComment comment, ForumArticle article, ForumComment pComment) {
        Integer integral = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
        if (integral > 0) {
            userInfoService.updateUserIntegral(comment.getUserId(), UserIntegralOperTypeEnum.POST_COMMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), integral);
        }
        if (comment.getpCommentId() == 0) {
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(), Constants.ONE, comment.getArticleId());
        }

        UserMessage userMessage = new UserMessage();
        userMessage.setMessageType(MessageTypeEnum.COMMENT.getType());
        userMessage.setCreateTime(new Date());
        userMessage.setArticleId(comment.getArticleId());
        userMessage.setCommentId(comment.getCommentId());
        userMessage.setSendUserId(comment.getUserId());
        userMessage.setSendNickName(comment.getNickName());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setArticleTitle(article.getTitle());
        if (comment.getpCommentId() == 0) {
            userMessage.setReceivedUserId(article.getUserId());
        } else if (comment.getpCommentId() != 0 && StringTools.isEmpty(comment.getReplyUserId())) {
            userMessage.setReceivedUserId(pComment.getUserId());
        } else if (comment.getpCommentId() != 0 && !StringTools.isEmpty(comment.getReplyUserId())) {
            userMessage.setReceivedUserId(comment.getReplyUserId());
        }
        if (!comment.getUserId().equals(userMessage.getReceivedUserId())) {
            userMessageService.add(userMessage);
        }
    }
}