package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleOrderTypeEnum;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.OperRecordOpTypeEnum;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.ForumArticleAttachmentQuery;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.*;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.CopyTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

}
