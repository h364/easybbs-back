package com.easybbs.service.impl;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumArticleAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.ForumArticleAttachmentDownloadQuery;
import com.easybbs.entity.po.ForumArticleAttachmentDownload;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.ForumArticleAttachmentDownloadMapper;
import com.easybbs.service.ForumArticleAttachmentDownloadService;
import com.easybbs.utils.StringTools;


/**
 * 用户附件下载 业务接口实现
 */
@Slf4j
@Service("forumArticleAttachmentDownloadService")
public class ForumArticleAttachmentDownloadServiceImpl implements ForumArticleAttachmentDownloadService {

    @Resource
    private ForumArticleAttachmentDownloadMapper<ForumArticleAttachmentDownload, ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadMapper;

    @Resource
    private ForumArticleAttachmentService forumArticleAttachmentService;

    @Resource
    private WebConfig webConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ForumArticleAttachmentDownload> findListByParam(ForumArticleAttachmentDownloadQuery param) {
        return this.forumArticleAttachmentDownloadMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ForumArticleAttachmentDownloadQuery param) {
        return this.forumArticleAttachmentDownloadMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ForumArticleAttachmentDownload> findListByPage(ForumArticleAttachmentDownloadQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ForumArticleAttachmentDownload> list = this.findListByParam(param);
        PaginationResultVO<ForumArticleAttachmentDownload> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ForumArticleAttachmentDownload bean) {
        return this.forumArticleAttachmentDownloadMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ForumArticleAttachmentDownload> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleAttachmentDownloadMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ForumArticleAttachmentDownload> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.forumArticleAttachmentDownloadMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ForumArticleAttachmentDownload bean, ForumArticleAttachmentDownloadQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleAttachmentDownloadMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ForumArticleAttachmentDownloadQuery param) {
        StringTools.checkParam(param);
        return this.forumArticleAttachmentDownloadMapper.deleteByParam(param);
    }

    /**
     * 根据FileIdAndUserId获取对象
     */
    @Override
    public ForumArticleAttachmentDownload getForumArticleAttachmentDownloadByFileIdAndUserId(String fileId, String userId) {
        return this.forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId, userId);
    }

    /**
     * 根据FileIdAndUserId修改
     */
    @Override
    public Integer updateForumArticleAttachmentDownloadByFileIdAndUserId(ForumArticleAttachmentDownload bean, String fileId, String userId) {
        return this.forumArticleAttachmentDownloadMapper.updateByFileIdAndUserId(bean, fileId, userId);
    }

    /**
     * 根据FileIdAndUserId删除
     */
    @Override
    public Integer deleteForumArticleAttachmentDownloadByFileIdAndUserId(String fileId, String userId) {
        return this.forumArticleAttachmentDownloadMapper.deleteByFileIdAndUserId(fileId, userId);
    }

    @Override
    public void attachmentDownLoad(HttpServletRequest request, HttpServletResponse response, String fileId, SessionWebUserDto userDto) {
        ForumArticleAttachment attachment = forumArticleAttachmentService.downloadAttachment(fileId, userDto);
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = attachment.getFileName();
        String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + attachment.getFilePath();
        File file = new File(filePath);
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            response.setContentType("application/x-msdownload; charset=UTF-8");
            // 解决中文文件名乱码问题
            if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0) {//IE浏览器
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
            } else {
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\"");
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len); // write
            }
            out.flush();
        } catch (Exception e) {
            log.error("下载异常", e);
            throw new BusinessException("下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                log.error("IO异常", e);
            }
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                log.error("IO异常", e);
            }
        }
    }
}