package com.easybbs.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easybbs.constants.Constants;
import com.easybbs.dto.FileUploadDto;
import com.easybbs.dto.SysSetting4AuditDto;
import com.easybbs.entity.config.AppConfig;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.ForumArticleAttachmentMapper;
import com.easybbs.service.ForumBoardService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.FileUtils;
import com.easybbs.utils.ImageUtils;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.stereotype.Service;

import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.ForumArticleMapper;
import com.easybbs.service.ForumArticleService;
import com.easybbs.utils.StringTools;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文章信息 业务接口实现
 */
@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

    @Resource
    private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

    @Resource
    private ForumArticleAttachmentMapper<ForumArticleAttachment, ForumArticleAttachmentQuery> forumArticleAttachmentMapper;

    @Resource
    private ForumBoardService forumBoardService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private FileUtils fileUtils;

    @Resource
    private ImageUtils imageUtils;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ForumArticle> findListByParam(ForumArticleQuery param) {
        return this.forumArticleMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ForumArticleQuery param) {
        return this.forumArticleMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ForumArticle> list = this.findListByParam(param);
        PaginationResultVO<ForumArticle> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ForumArticle bean) {
        return this.forumArticleMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ForumArticle> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ForumArticle> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ForumArticle bean, ForumArticleQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ForumArticleQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleMapper.deleteByParam(param);
    }

    /**
     * 根据ArticleId获取对象
     */
    @Override
    public ForumArticle getForumArticleByArticleId(String articleId) {
        return this.forumArticleMapper.selectByArticleId(articleId);
    }

    /**
     * 根据ArticleId修改
     */
    @Override
    public Integer updateForumArticleByArticleId(ForumArticle bean, String articleId) {
        return this.forumArticleMapper.updateByArticleId(bean, articleId);
    }

    /**
     * 根据ArticleId删除
     */
    @Override
    public Integer deleteForumArticleByArticleId(String articleId) {
        return this.forumArticleMapper.deleteByArticleId(articleId);
    }

    @Override
    public ForumArticle readArticle(String articleId) {
        ForumArticle forumArticle = this.forumArticleMapper.selectByArticleId(articleId);
        if (forumArticle == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        if (ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())) {
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), Constants.ONE, articleId);
        }
        return forumArticle;
    }

    @Override
    public void postArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
        resetBoardInfo(isAdmin, forumArticle);

        Date curDate = new Date();
        String articleId = StringTools.getRandomString(Constants.LENGTH_15);
        forumArticle.setArticleId(articleId);
        forumArticle.setPostTime(curDate);
        forumArticle.setLastUpdateTime(curDate);

        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            forumArticle.setCover(fileUploadDto.getLocalPath());
        }

        if (attachment != null) {
            uploadAttachment(forumArticle, forumArticleAttachment, attachment, false);
            forumArticle.setAttachmentType(ArticleAttachmentTypeEnum.YES.getType());
        } else {
            forumArticle.setAttachmentType(ArticleAttachmentTypeEnum.NO.getType());
        }

        if (isAdmin) {
            forumArticle.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        } else {
            SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
            forumArticle.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.NO_AUDIT.getStatus() : ArticleStatusEnum.AUDIT.getStatus());
        }
        //替换图片
        String content = forumArticle.getContent();
        if (!StringTools.isEmpty(content)) {
            String month = imageUtils.resetImageHtml(content);
            String replaceMonth = "/" + month + "/";
            content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
            forumArticle.setContent(content);
            String markdownContent = forumArticle.getMarkdownContent();
            if (!StringTools.isEmpty(markdownContent)) {
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
                forumArticle.setMarkdownContent(markdownContent);
            }
        }

        forumArticleMapper.insert(forumArticle);
        Integer postIntegral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
        if (postIntegral > 0 && ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())) {
            userInfoService.updateUserIntegral(forumArticle.getUserId(), UserIntegralOperTypeEnum.POST_ARTICLE, UserIntegralChangeTypeEnum.ADD.getChangeType(), postIntegral);
        }
    }

    @Override
    public void updateArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
        ForumArticle dbInfo = forumArticleMapper.selectByArticleId(forumArticle.getArticleId());
        if (!isAdmin && !dbInfo.getUserId().equals(dbInfo.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        forumArticle.setLastUpdateTime(new Date());
        resetBoardInfo(isAdmin, forumArticle);
        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            forumArticle.setCover(fileUploadDto.getLocalPath());
        }
        if (attachment != null) {
            uploadAttachment(forumArticle, forumArticleAttachment, attachment, true);
            forumArticle.setAttachmentType(ArticleAttachmentTypeEnum.YES.getType());
        }
        //获取数据库中的附件
        ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
        attachmentQuery.setArticleId(forumArticle.getArticleId());
        List<ForumArticleAttachment> articleAttachmentList = forumArticleAttachmentMapper.selectList(attachmentQuery);
        ForumArticleAttachment dbAttachment = null;
        if (!articleAttachmentList.isEmpty()) {
            dbAttachment = articleAttachmentList.get(0);
        }

        if (dbAttachment != null) {
            if (forumArticle.getAttachmentType() == 0) {
                //删除之前的附件
                new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbAttachment.getFilePath()).delete();
                forumArticleAttachmentMapper.deleteByFileId(dbAttachment.getFileId());
            } else {
                //更新积分
                if (!dbAttachment.getIntegral().equals(forumArticleAttachment.getIntegral())) {
                    ForumArticleAttachment integralUpdate = new ForumArticleAttachment();
                    integralUpdate.setIntegral(forumArticleAttachment.getIntegral());
                    forumArticleAttachmentMapper.updateByFileId(integralUpdate, dbAttachment.getFileId());
                }
            }
        }
        //文章是否需要审核
        if (isAdmin) {
            forumArticle.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        } else {
            SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
            forumArticle.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.NO_AUDIT.getStatus() :
                    ArticleStatusEnum.AUDIT.getStatus());
        }

        //替换图片
        String content = forumArticle.getContent();
        if (!StringTools.isEmpty(content)) {
            String month = imageUtils.resetImageHtml(content);
            //避免替换博客中template关键，所以前后带上/
            String replaceMonth = "/" + month + "/";
            content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
            forumArticle.setContent(content);
            String markdownContent = forumArticle.getMarkdownContent();
            if (!StringTools.isEmpty(markdownContent)) {
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
                forumArticle.setMarkdownContent(markdownContent);
            }
        }

        this.forumArticleMapper.updateByArticleId(forumArticle, forumArticle.getArticleId());
    }

    private void resetBoardInfo(Boolean isAdmin, ForumArticle forumArticle) {
        ForumBoard pBoard = forumBoardService.getForumBoardByBoardId(forumArticle.getpBoardId());
        if (pBoard == null || pBoard.getPostType() == Constants.ZERO && !isAdmin) {
            throw new BusinessException("一级板块不存在");
        }
        forumArticle.setpBoardName(pBoard.getBoardName());
        if (forumArticle.getBoardId() != null && forumArticle.getBoardId() != 0) {
            ForumBoard board = forumBoardService.getForumBoardByBoardId(forumArticle.getpBoardId());
            if (board == null || board.getPostType() == Constants.ZERO && !isAdmin) {
                throw new BusinessException("二级板块不存在");
            }
            forumArticle.setBoardName(board.getBoardName());
        } else {
            forumArticle.setBoardId(0);
            forumArticle.setBoardName("");
        }
    }

    private void uploadAttachment(ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile attachment, Boolean isUpdate) {
        Integer allowSizeMb = SysCacheUtils.getSysSetting().getPostSetting().getAttachmentSize();
        Integer allowSize = allowSizeMb * Constants.FILE_SIZE_1M;
        if (attachment.getSize() > allowSize) {
            throw new BusinessException("附件只能上传" + allowSize + "MB");
        }
        //修改
        ForumArticleAttachment dbInfo = null;
        if (isUpdate) {
            ForumArticleAttachmentQuery articleAttachmentQuery = new ForumArticleAttachmentQuery();
            articleAttachmentQuery.setArticleId(forumArticle.getArticleId());
            List<ForumArticleAttachment> articleAttachmentList = forumArticleAttachmentMapper.selectList(articleAttachmentQuery);
            if (!articleAttachmentList.isEmpty()) {
                dbInfo = articleAttachmentList.get(0);
                new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbInfo.getFilePath()).delete();
            }
        }
        FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(attachment, Constants.FILE_FOLDER_ATTACHMENT, FileUploadTypeEnum.ARTICLE_ATTACHMENT);
        if (dbInfo == null) {
            forumArticleAttachment.setFileId(StringTools.getRandomNumber(Constants.LENGTH_15));
            forumArticleAttachment.setArticleId(forumArticle.getArticleId());
            forumArticleAttachment.setFileName(fileUploadDto.getOriginalFileName());
            forumArticleAttachment.setFilePath(fileUploadDto.getLocalPath());
            forumArticleAttachment.setFileSize(attachment.getSize());
            forumArticleAttachment.setDownloadCount(Constants.ZERO);
            forumArticleAttachment.setFileType(AttachmentFileTypeEnum.ZIP.getType());
            forumArticleAttachment.setUserId(forumArticle.getUserId());
            forumArticleAttachmentMapper.insert(forumArticleAttachment);
        } else {
            ForumArticleAttachment updateInfo = new ForumArticleAttachment();
            updateInfo.setFileName(fileUploadDto.getOriginalFileName());
            updateInfo.setFileSize(attachment.getSize());
            updateInfo.setFilePath(fileUploadDto.getLocalPath());
            forumArticleAttachmentMapper.updateByFileId(updateInfo, dbInfo.getFileId());
        }
    }
}