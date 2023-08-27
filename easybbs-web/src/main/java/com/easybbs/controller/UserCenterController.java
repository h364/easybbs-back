package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.dto.UserMessageCountDto;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.MessageTypeEnum;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.UserStatusEnum;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.LikeRecordQuery;
import com.easybbs.entity.query.UserIntegralRecordQuery;
import com.easybbs.entity.query.UserMessageQuery;
import com.easybbs.entity.vo.ForumArticleVO;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.UserInfoVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.*;
import com.easybbs.utils.CopyTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/ucenter")
public class UserCenterController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private UserIntegralRecordService userIntegralRecordService;

    @Resource
    private UserMessageService userMessageService;

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getUserInfo(@VerifyParam(required = true) String userId) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (userInfo == null || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setUserId(userId);
        articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        Integer postCount = forumArticleService.findCountByParam(articleQuery);

        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setPostCount(postCount);
        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVO.setLikeCount(likeCount);
        return getSuccessResponseVO(userInfoVO);
    }

    @RequestMapping("/loadUserArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserArticle(HttpSession session, @VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer type, Integer pageNo) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (userInfo == null || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setOrderBy("post_time desc");
        articleQuery.setPageNo(pageNo);

        if (type == 0) {
            articleQuery.setUserId(userId);
        } else if (type == 1) {
            articleQuery.setCommentUserId(userId);
        } else if (type == 2) {
            articleQuery.setLikeUserId(userId);
        }
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null) {
            articleQuery.setCurrentUserId(userDto.getUserId());
        } else {
            articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }
        PaginationResultVO<ForumArticle> resultVO = forumArticleService.findListByPage(articleQuery);

        return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateUserInfo(HttpSession session, Integer sex, @VerifyParam(max = 100) String personDescription, MultipartFile avatar) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userDto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPersonDescription(personDescription);
        userInfoService.updateUserInfo(userInfo, avatar);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("loadUserIntegralRecord")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserIntegralRecord(HttpSession session, Integer pageNo, String createTimeStart, String createTimeEnd) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        UserIntegralRecordQuery recordQuery = new UserIntegralRecordQuery();
        recordQuery.setUserId(userDto.getUserId());
        recordQuery.setPageNo(pageNo);
        recordQuery.setCreateTimeStart(createTimeStart);
        recordQuery.setCreateTimeEnd(createTimeEnd);
        recordQuery.setOrderBy("record_id desc");
        PaginationResultVO<UserIntegralRecord> resultVO = userIntegralRecordService.findListByPage(recordQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getMessageCount")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getMessageCount(HttpSession session) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        UserMessageCountDto userMessageCountDto = userMessageService.getMessageCount(userDto.getUserId());
        return getSuccessResponseVO(userMessageCountDto);
    }

    @RequestMapping("loadMessageList")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO loadMessageList(HttpSession session, @VerifyParam(required = true) String code, Integer pageNo) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        MessageTypeEnum typeEnum = MessageTypeEnum.getByCode(code);
        if (typeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserMessageQuery messageQuery = new UserMessageQuery();
        messageQuery.setPageNo(pageNo);
        messageQuery.setReceivedUserId(userDto.getUserId());
        messageQuery.setMessageType(typeEnum.getType());
        messageQuery.setOrderBy("message_id desc");
        PaginationResultVO<UserMessage> resultVO = userMessageService.findListByPage(messageQuery);

        if (pageNo == null || pageNo == 1) {
            userMessageService.readMessageByType(userDto.getUserId(), typeEnum.getType());
        }
        return getSuccessResponseVO(resultVO);
    }
}
