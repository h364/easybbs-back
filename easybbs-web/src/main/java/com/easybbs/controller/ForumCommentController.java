package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.component.RedisComponent;
import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumCommentService;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.StringTools;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class ForumCommentController extends ABaseController {

    @Resource
    private ForumCommentService forumCommentService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadComment(HttpSession session, @VerifyParam(required = true) String articleId, Integer pageNo, Integer orderType) {
        final String ORDER_TYPE0 = "good_count desc, comment_id asc";
        final String ORDER_TYPE1 = "comment_id desc";

        if (!redisComponent.getSysSetting().getCommentSetting().getCommentOpen()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        ForumCommentQuery query = new ForumCommentQuery();
        query.setArticleId(articleId);
        String orderBy = orderType == null || orderType == Constants.ZERO ? ORDER_TYPE0 : ORDER_TYPE1;
        query.setOrderBy("top_type desc," + orderBy);
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        if (webUserDto != null) {
            query.setQueryLikeType(true);
            query.setCurrentUserId(webUserDto.getUserId());
        } else {
            query.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }
        query.setPageNo(pageNo);
        query.setPageSize(PageSize.SIZE50.getSize());
        query.setpCommentId(Constants.ZERO);
        query.setLoadChildren(true);

        PaginationResultVO<ForumComment> forumCommentList = forumCommentService.findListByPage(query);
        return getSuccessResponseVO(forumCommentList);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO doLike(HttpSession session, @VerifyParam(required = true) Integer commentId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        String objectId = String.valueOf(commentId);
        likeRecordService.doLike(objectId, webUserDto.getUserId(), webUserDto.getNickname(), OperRecordOpTypeEnum.COMMENT_LIKE);
        LikeRecord likeRecord = likeRecordService.getUserOperRecordByObjectIdAndUserIdAndOpType(objectId, webUserDto.getUserId(), OperRecordOpTypeEnum.COMMENT_LIKE.getType());
        ForumComment comment = forumCommentService.getForumCommentByCommentId(commentId);
        comment.setLikeType(likeRecord == null ? 0 : 1);
        return getSuccessResponseVO(comment);
    }

    @RequestMapping("/changeTopType")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO changeTopType(HttpSession session,
                                    @VerifyParam(required = true) Integer commentId,
                                    @VerifyParam(required = true) Integer topType) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        forumCommentService.changeTopType(webUserDto.getUserId(), commentId, topType);

        return getSuccessResponseVO(null);
    }

    @RequestMapping("/postComment")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO postComment(HttpSession session,
                                  @VerifyParam(required = true) String articleId,
                                  @VerifyParam(required = true) Integer pCommentId,
                                  @VerifyParam(min = 1, max = 500) String content,
                                  MultipartFile image,
                                  String replyUserId) {
        if (!redisComponent.getSysSetting().getCommentSetting().getCommentOpen()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(image == null && StringTools.isEmpty(content)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        content = StringTools.escapeHtml(content);
        ForumComment forumComment = new ForumComment();
        forumComment.setUserId(webUserDto.getUserId());
        forumComment.setNickName(webUserDto.getNickname());
        forumComment.setUserIpAddress(webUserDto.getProvince());
        forumComment.setpCommentId(pCommentId);
        forumComment.setArticleId(articleId);
        forumComment.setContent(content);
        forumComment.setReplyUserId(replyUserId);
        forumComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        forumCommentService.postComment(forumComment, image);

        if(pCommentId != 0) {
            ForumCommentQuery query = new ForumCommentQuery();
            query.setArticleId(articleId);
            query.setpCommentId(pCommentId);
            query.setOrderBy("comment_id asc");
            List<ForumComment> children = forumCommentService.findListByParam(query);
            return getSuccessResponseVO(children);
        }

        return getSuccessResponseVO(null);
    }
}
