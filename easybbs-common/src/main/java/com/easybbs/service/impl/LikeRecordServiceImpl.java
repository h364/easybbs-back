package com.easybbs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.Resource;

import com.easybbs.constants.Constants;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.*;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.mappers.ForumCommentMapper;
import com.easybbs.mappers.UserMessageMapper;
import org.springframework.stereotype.Service;

import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.mappers.LikeRecordMapper;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 点赞记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

    @Resource
    private LikeRecordMapper<LikeRecord, LikeRecordQuery> likeRecordMapper;

    @Resource
    private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

    @Resource
    private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

    @Resource
    private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<LikeRecord> findListByParam(LikeRecordQuery param) {
        return this.likeRecordMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(LikeRecordQuery param) {
        return this.likeRecordMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<LikeRecord> list = this.findListByParam(param);
        PaginationResultVO<LikeRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(LikeRecord bean) {
        return this.likeRecordMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<LikeRecord> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.likeRecordMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.likeRecordMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(LikeRecord bean, LikeRecordQuery param) {
        StringTools.checkParam(param);
        return this.likeRecordMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(LikeRecordQuery param) {
        StringTools.checkParam(param);
        return this.likeRecordMapper.deleteByParam(param);
    }

    /**
     * 根据OpId获取对象
     */
    @Override
    public LikeRecord getLikeRecordByOpId(Integer opId) {
        return this.likeRecordMapper.selectByOpId(opId);
    }

    /**
     * 根据OpId修改
     */
    @Override
    public Integer updateLikeRecordByOpId(LikeRecord bean, Integer opId) {
        return this.likeRecordMapper.updateByOpId(bean, opId);
    }

    /**
     * 根据OpId删除
     */
    @Override
    public Integer deleteLikeRecordByOpId(Integer opId) {
        return this.likeRecordMapper.deleteByOpId(opId);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType获取对象
     */
    @Override
    public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType修改
     */
    @Override
    public Integer updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
    }

    /**
     * 根据ObjectIdAndUserIdAndOpType删除
     */
    @Override
    public Integer deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
        return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
    }

    @Override
    public LikeRecord getUserOperRecordByObjectIdAndUserIdAndOpType(String articleId, String userId, Integer type) {
        return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(articleId, userId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doLike(String objectId, String userId, String nickname, OperRecordOpTypeEnum opTypeEnum) {
        UserMessage userMessage = new UserMessage();
        userMessage.setCreateTime(new Date());
        switch (opTypeEnum) {
            case ARTICLE_LIKE:
                ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
                if (forumArticle == null) {
                    throw new BusinessException("文章不存在");
                }
                articleLike(objectId, forumArticle, userId, opTypeEnum);

                userMessage.setArticleId(objectId);
                userMessage.setArticleTitle(forumArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
                userMessage.setCommentId(Constants.ZERO);
                userMessage.setReceivedUserId(forumArticle.getUserId());
                break;
            case COMMENT_LIKE:
                ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
                if (forumComment == null) {
                    throw new BusinessException("评论不存在");
                }
                commentLike(objectId, forumComment, userId, opTypeEnum);

                forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());

                userMessage.setArticleId(objectId);
                userMessage.setArticleTitle(forumArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
                userMessage.setCommentId(forumComment.getCommentId());
                userMessage.setReceivedUserId(forumComment.getReplyUserId());
                userMessage.setMessageContent(forumComment.getContent());
                break;

        }
        userMessage.setSendUserId(userId);
        userMessage.setSendNickName(nickname);
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        if (!userId.equals(userMessage.getReceivedUserId())) {
            UserMessage message = userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(objectId, userMessage.getCommentId(), userMessage.getSendUserId(), userMessage.getMessageType());
            if (message == null) {
                userMessageMapper.insert(userMessage);
            }

        }
    }

    private LikeRecord articleLike(String articleId, ForumArticle forumArticle, String userId, OperRecordOpTypeEnum opTypeEnum) {
        LikeRecord likeRecord = likeRecordMapper.selectByObjectIdAndUserIdAndOpType(articleId, userId, opTypeEnum.getType());
        if (likeRecord != null) {
            likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(articleId, userId, opTypeEnum.getType());
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), -1, articleId);
        } else {
            LikeRecord record = new LikeRecord();
            record.setObjectId(articleId);
            record.setUserId(userId);
            record.setOpType(opTypeEnum.getType());
            record.setCreateTime(new Date());
            record.setAuthorUserId(forumArticle.getUserId());
            likeRecordMapper.insert(record);
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), Constants.ONE, articleId);
        }
        return likeRecord;
    }

    private void commentLike(String commentId, ForumComment forumComment, String userId, OperRecordOpTypeEnum opTypeEnum) {
        LikeRecord likeRecord = likeRecordMapper.selectByObjectIdAndUserIdAndOpType(commentId, userId, opTypeEnum.getType());
        if (likeRecord != null) {
            likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(commentId, userId, opTypeEnum.getType());
            forumCommentMapper.updateArticleCount(-1, Integer.parseInt(commentId));
        } else {
            LikeRecord record = new LikeRecord();
            record.setObjectId(commentId);
            record.setUserId(userId);
            record.setOpType(opTypeEnum.getType());
            record.setCreateTime(new Date());
            record.setAuthorUserId(forumComment.getUserId());
            likeRecordMapper.insert(record);
            forumCommentMapper.updateArticleCount(1, Integer.parseInt(commentId));
        }
    }
}