package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.*;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.*;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.*;
import com.easybbs.utils.CopyTools;
import com.easybbs.utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private ForumArticleAttachmentService forumArticleAttachmentService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;

    @Resource
    private ForumBoardService forumBoardService;

    @RequestMapping("/loadArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadArticle(HttpSession session, Integer boardId, Integer pBoardId, Integer orderType, Integer pageNo) {
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setBoardId(boardId);
        articleQuery.setpBoardId(pBoardId);
        articleQuery.setPageNo(pageNo);
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        if (webUserDto != null) {
            articleQuery.setCurrentUserId(webUserDto.getUserId());
        } else {
            articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }
        ArticleOrderTypeEnum articleOrderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
        articleOrderTypeEnum = articleOrderTypeEnum == null ? ArticleOrderTypeEnum.HOT : articleOrderTypeEnum;
        articleQuery.setOrderBy(articleOrderTypeEnum.getOrderSql());
        PaginationResultVO<ForumArticle> resultVo = forumArticleService.findListByPage(articleQuery);

        return getSuccessResponseVO(convert2PaginationVO(resultVo, ForumArticleVO.class));
    }

    @RequestMapping("/getArticleDetail")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getArticleDetail(HttpSession session, @VerifyParam(required = true) String articleId) {
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);

        ForumArticle forumArticle = forumArticleService.readArticle(articleId);

        if (forumArticle == null
                || (ArticleStatusEnum.NO_AUDIT.getStatus().equals(forumArticle.getStatus())
                && (sessionWebUserDto == null || !sessionWebUserDto.getUserId().equals(forumArticle.getUserId()) && !sessionWebUserDto.getAdmin()))
                || ArticleStatusEnum.DEL.getStatus().equals(forumArticle.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

        if (forumArticle.getAttachmentType() == Constants.ONE) {
            ForumArticleAttachmentQuery articleAttachmentQuery = new ForumArticleAttachmentQuery();
            articleAttachmentQuery.setArticleId(forumArticle.getArticleId());
            List<ForumArticleAttachment> forumArticleAttachmentList = forumArticleAttachmentService.findListByParam(articleAttachmentQuery);
            if (!forumArticleAttachmentList.isEmpty()) {
                detailVO.setAttachment(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
            }
        }

        if (sessionWebUserDto != null) {
            LikeRecord like = likeRecordService.getUserOperRecordByObjectIdAndUserIdAndOpType(articleId, sessionWebUserDto.getUserId(),
                    OperRecordOpTypeEnum.ARTICLE_LIKE.getType());
            if (like != null) {
                detailVO.setHaveLike(true);
            }
        }
        return getSuccessResponseVO(detailVO);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO doLike(HttpSession session, @VerifyParam(required = true) String articleId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        likeRecordService.doLike(articleId, webUserDto.getUserId(), webUserDto.getNickname(), OperRecordOpTypeEnum.ARTICLE_LIKE);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getUserDownLoadInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO getUserDownLoadInfo(HttpSession session, @VerifyParam(required = true) String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(webUserDto.getUserId());
        UserDownLoadVO userDownLoadVO = new UserDownLoadVO();
        userDownLoadVO.setUserIntegral(userInfo.getCurrentIntegral());
        ForumArticleAttachmentDownload attachmentDownload = forumArticleAttachmentDownloadService.getForumArticleAttachmentDownloadByFileIdAndUserId(fileId, webUserDto.getUserId());
        if (attachmentDownload != null) {
            userDownLoadVO.setHaveDownLoad(true);
        }
        return getSuccessResponseVO(userDownLoadVO);
    }

    @RequestMapping("/attachmentDownLoad")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO attachmentDownLoad(HttpSession session, HttpServletRequest request, HttpServletResponse response, @VerifyParam(required = true) String fileId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        forumArticleAttachmentDownloadService.attachmentDownLoad(request, response, fileId, userDto);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadBoard4Post")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadBoard4Post(HttpSession session) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        Integer postType = null;
        if (userDto.getAdmin()) {
            postType = Constants.ONE;
        }
        return getSuccessResponseVO(forumBoardService.getBoardTree(postType));
    }

    @RequestMapping("/postArticle")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO postArticle(HttpSession session,
                                  MultipartFile cover,
                                  MultipartFile attachment,
                                  Integer integral,
                                  Integer boardId,
                                  @VerifyParam(required = true, max = 150) String title,
                                  @VerifyParam(required = true) Integer pBoardId,
                                  @VerifyParam(max = 200) String summary,
                                  @VerifyParam(required = true) Integer editorType,
                                  @VerifyParam(required = true) String content,
                                  String markdownContent) {
        title = StringTools.escapeHtml(title);
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setpBoardId(pBoardId);
        forumArticle.setBoardId(boardId);
        forumArticle.setTitle(title);
        forumArticle.setContent(content);
        forumArticle.setSummary(summary);

        EditorTypeEnum editorTypeEnum = EditorTypeEnum.getByType(editorType);
        if (editorTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (EditorTypeEnum.MARKDOWN.getType().equals(editorType) && StringTools.isEmpty(markdownContent)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setEditorType(editorType);
        forumArticle.setUserId(userDto.getUserId());
        forumArticle.setNickName(userDto.getNickname());
        forumArticle.setUserIpAddress(userDto.getProvince());

        //附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral == null ? 0 : integral);
        forumArticleService.postArticle(userDto.getAdmin(), forumArticle, forumArticleAttachment, cover, attachment);
        return getSuccessResponseVO(forumArticle.getArticleId());
    }

    @RequestMapping("/articleDetail4Update")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO articleDetail4Update(HttpSession session, @VerifyParam(required = true) String articleId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        ForumArticle forumArticle = forumArticleService.getForumArticleByArticleId(articleId);
        if (forumArticle == null || !userDto.getUserId().equals(forumArticle.getUserId())) {
            throw new BusinessException("文章不存在或你无权编辑该文章");
        }
        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

        if (forumArticle.getAttachmentType() == Constants.ONE) {
            ForumArticleAttachmentQuery articleAttachmentQuery = new ForumArticleAttachmentQuery();
            articleAttachmentQuery.setArticleId(forumArticle.getArticleId());
            List<ForumArticleAttachment> forumArticleAttachmentList = forumArticleAttachmentService.findListByParam(articleAttachmentQuery);
            if (!forumArticleAttachmentList.isEmpty()) {
                detailVO.setAttachment(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
            }
        }
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/updateArticle")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO updateArticle(HttpSession session,
                                    MultipartFile cover,
                                    MultipartFile attachment,
                                    Integer integral,
                                    Integer boardId,
                                    @VerifyParam(required = true) String articleId,
                                    @VerifyParam(required = true, max = 150) String title,
                                    @VerifyParam(required = true) Integer pBoardId,
                                    @VerifyParam(max = 200) String summary,
                                    @VerifyParam(required = true) Integer editorType,
                                    @VerifyParam(required = true) String content,
                                    String markdownContent,
                                    Integer attachmentType) {
        title = StringTools.escapeHtml(title);
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setArticleId(articleId);
        forumArticle.setpBoardId(pBoardId);
        forumArticle.setBoardId(boardId);
        forumArticle.setTitle(title);
        forumArticle.setContent(content);
        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setSummary(summary);
        forumArticle.setEditorType(editorType);
        forumArticle.setUserIpAddress(userDto.getProvince());
        forumArticle.setAttachmentType(attachmentType);
        forumArticle.setUserId(userDto.getUserId());

        //附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral == null ? 0 : integral);
        forumArticleService.updateArticle(userDto.getAdmin(), forumArticle, forumArticleAttachment, cover, attachment);
        return getSuccessResponseVO(forumArticle.getArticleId());
    }

    @RequestMapping("/search")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO search(@VerifyParam(required = true, min = 2) String keyword) {
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setTitleFuzzy(keyword);
        PaginationResultVO<ForumArticle> resultVO = forumArticleService.findListByPage(articleQuery);
        return getSuccessResponseVO(resultVO);
    }

}
