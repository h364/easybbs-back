package com.easybbs.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.*;
import com.easybbs.entity.query.ForumArticleAttachmentDownloadQuery;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleAttachmentDownloadMapper;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.UserInfoService;
import com.easybbs.service.UserMessageService;
import org.springframework.stereotype.Service;

import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.ForumArticleAttachmentMapper;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 文件信息 业务接口实现
 */
@Service("forumArticleAttachmentService")
public class ForumArticleAttachmentServiceImpl implements ForumArticleAttachmentService {

    @Resource
    private ForumArticleAttachmentMapper<ForumArticleAttachment, ForumArticleAttachmentQuery> forumArticleAttachmentMapper;

    @Resource
    private ForumArticleAttachmentDownloadMapper<ForumArticleAttachmentDownload, ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private UserMessageService userMessageService;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ForumArticleAttachment> findListByParam(ForumArticleAttachmentQuery param) {
        return this.forumArticleAttachmentMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ForumArticleAttachmentQuery param) {
        return this.forumArticleAttachmentMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ForumArticleAttachment> findListByPage(ForumArticleAttachmentQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ForumArticleAttachment> list = this.findListByParam(param);
        PaginationResultVO<ForumArticleAttachment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ForumArticleAttachment bean) {
        return this.forumArticleAttachmentMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ForumArticleAttachment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleAttachmentMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ForumArticleAttachment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleAttachmentMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ForumArticleAttachment bean, ForumArticleAttachmentQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleAttachmentMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ForumArticleAttachmentQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleAttachmentMapper.deleteByParam(param);
    }

    /**
     * 根据FileId获取对象
     */
    @Override
    public ForumArticleAttachment getForumArticleAttachmentByFileId(String fileId) {
        return this.forumArticleAttachmentMapper.selectByFileId(fileId);
    }

    /**
     * 根据FileId修改
     */
    @Override
    public Integer updateForumArticleAttachmentByFileId(ForumArticleAttachment bean, String fileId) {
        return this.forumArticleAttachmentMapper.updateByFileId(bean, fileId);
    }

    /**
     * 根据FileId删除
     */
    @Override
    public Integer deleteForumArticleAttachmentByFileId(String fileId) {
        return this.forumArticleAttachmentMapper.deleteByFileId(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ForumArticleAttachment downloadAttachment(String fileId, SessionWebUserDto webDto) {
        ForumArticleAttachment forumArticleAttachment = forumArticleAttachmentMapper.selectByFileId(fileId);
        if (forumArticleAttachment == null) {
            throw new BusinessException("附件不存在");
        }
        ForumArticleAttachmentDownload download = null;
        if (forumArticleAttachment.getIntegral() > 0 && !webDto.getUserId().equals(forumArticleAttachment.getUserId())) {
            download = forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId, webDto.getUserId());
            if (download == null) {
                UserInfo userInfo = userInfoService.getUserInfoByUserId(webDto.getUserId());
                if (userInfo.getCurrentIntegral() - forumArticleAttachment.getIntegral() < 0) {
                    throw new BusinessException("积分不够");
                }
            }
        }
        ForumArticleAttachmentDownload updateDownLoad = new ForumArticleAttachmentDownload();
        updateDownLoad.setArticleId(forumArticleAttachment.getArticleId());
        updateDownLoad.setUserId(webDto.getUserId());
        updateDownLoad.setFileId(fileId);
        updateDownLoad.setDownloadCount(Constants.ONE);
        forumArticleAttachmentDownloadMapper.insertOrUpdate(updateDownLoad);

        forumArticleAttachmentMapper.updateDownLoadCount(fileId);

        if (webDto.getUserId().equals(forumArticleAttachment.getUserId()) || download != null) {
            return forumArticleAttachment;
        }
        //下载者扣除积分
        userInfoService.updateUserIntegral(webDto.getUserId(), UserIntegralOperTypeEnum.USER_DOWNLOAD_ATTACHMENT, UserIntegralChangeTypeEnum.REDUCE.getChangeType(), forumArticleAttachment.getIntegral());
        //附件提供者加对应积分
        userInfoService.updateUserIntegral(forumArticleAttachment.getUserId(), UserIntegralOperTypeEnum.DOWNLOAD_ATTACHMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), forumArticleAttachment.getIntegral());

        //记录消息
        ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(forumArticleAttachment.getArticleId());

        UserMessage userMessage = new UserMessage();
        userMessage.setMessageType(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
        userMessage.setCreateTime(new Date());
        userMessage.setArticleId(forumArticleAttachment.getArticleId());
        userMessage.setCommentId(0);
        userMessage.setSendUserId(webDto.getUserId());
        userMessage.setSendNickName(webDto.getNickname());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setReceivedUserId(forumArticleAttachment.getUserId());
        userMessage.setArticleTitle(forumArticle.getTitle());
        if (!webDto.getUserId().equals(forumArticleAttachment.getUserId())) {
            userMessageService.add(userMessage);
        }

        return forumArticleAttachment;
    }
}