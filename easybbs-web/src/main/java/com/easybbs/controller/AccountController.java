package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.anotation.VerifyParam;
import com.easybbs.constants.Constants;
import com.easybbs.dto.CreateImageCode;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.dto.SysSetting4CommentDto;
import com.easybbs.dto.SysSettingDto;
import com.easybbs.entity.enums.VerifyRegexEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class AccountController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private EmailCodeService emailCodeService;

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();

        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/sendEmailCode")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO sendEmailCode(HttpSession session,
                                    @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL) String email,
                                    @VerifyParam(required = true) String checkCode,
                                    @VerifyParam(required = true) Integer type) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException("验证码错误");
            }

            emailCodeService.sendEmailCode(email, type);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }
    }

    @RequestMapping("/register")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO register(HttpSession session,
                               @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL) String email,
                               @VerifyParam(required = true) String emailCode,
                               @VerifyParam(required = true, max = 20) String nickname,
                               @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("验证码错误");
            }
            userInfoService.register(email, emailCode, nickname, password);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @RequestMapping("/login")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO login(HttpSession session,
                            HttpServletRequest request,
                            @VerifyParam(required = true) String email,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("验证码错误");
            }
            String ip = getIpAddr(request);
            SessionWebUserDto webDto = userInfoService.login(email, password, ip);
            session.setAttribute(Constants.SESSION_KEY, webDto);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor()
    public ResponseVO getUserInfo(HttpSession session) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        return getSuccessResponseVO(webUserDto);
    }

    @RequestMapping("/logout")
    @GlobalInterceptor()
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getSysSetting")
    @GlobalInterceptor()
    public ResponseVO getSysSetting() {
        SysSettingDto settingDto = SysCacheUtils.getSysSetting();
        SysSetting4CommentDto commentDto = settingDto.getCommentSetting();
        HashMap<String, Object> result = new HashMap<>();
        result.put("commentOpen", commentDto.getCommentOpen());
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/resetPwd")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO resetPwd(HttpSession session, String email) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }
}
