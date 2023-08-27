package com.easybbs.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.easybbs.component.RedisComponent;
import com.easybbs.constants.Constants;
import com.easybbs.dto.SessionWebUserDto;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserIntegralRecordQuery;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.UserIntegralRecordMapper;
import com.easybbs.mappers.UserMessageMapper;
import com.easybbs.service.EmailCodeService;
import com.easybbs.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.UserInfoMapper;
import com.easybbs.service.UserInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息 业务接口实现
 */
@Slf4j
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private UserMessageMapper userMessageMapper;

    @Resource
    private UserIntegralRecordMapper<UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordMapper;

    @Resource
    private WebConfig webConfig;

    @Resource
    private FileUtils fileUtils;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserInfo> findListByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(param);
        PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserInfo bean) {
        return this.userInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
        StringTools.checkParam(param);
        return this.userInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(UserInfoQuery param) {
        StringTools.checkParam(param);
        return this.userInfoMapper.deleteByParam(param);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return this.userInfoMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return this.userInfoMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteUserInfoByUserId(String userId) {
        return this.userInfoMapper.deleteByUserId(userId);
    }

    /**
     * 根据Email获取对象
     */
    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return this.userInfoMapper.selectByEmail(email);
    }

    /**
     * 根据Email修改
     */
    @Override
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return this.userInfoMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除
     */
    @Override
    public Integer deleteUserInfoByEmail(String email) {
        return this.userInfoMapper.deleteByEmail(email);
    }

    /**
     * 根据NickName获取对象
     */
    @Override
    public UserInfo getUserInfoByNickName(String nickName) {
        return this.userInfoMapper.selectByNickName(nickName);
    }

    /**
     * 根据NickName修改
     */
    @Override
    public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
        return this.userInfoMapper.updateByNickName(bean, nickName);
    }

    /**
     * 根据NickName删除
     */
    @Override
    public Integer deleteUserInfoByNickName(String nickName) {
        return this.userInfoMapper.deleteByNickName(nickName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String emailCode, String nickname, String password) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo != null) {
            throw new BusinessException("邮箱账号已存在");
        }
        UserInfo nickNameUser = userInfoMapper.selectByNickName(nickname);
        if (nickNameUser != null) {
            throw new BusinessException("昵称已存在");
        }

        emailCodeService.checkCode(email, emailCode);

        String userId = StringTools.getRandomNumber(Constants.LENGTH_10);

        userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setEmail(email);
        userInfo.setJoinTime(new Date());
        userInfo.setNickName(nickname);
        userInfo.setPassword(StringTools.encodeByMd5(password));
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfo.setTotalIntegral(Constants.ZERO);
        userInfo.setCurrentIntegral(Constants.ZERO);
        userInfoMapper.insert(userInfo);

        //更新用户积分
        updateUserIntegral(userId, UserIntegralOperTypeEnum.REGISTER, UserIntegralChangeTypeEnum.ADD.getChangeType(), Constants.INTEGRAL);
        //记录消息
        UserMessage userMessage = new UserMessage();
        userMessage.setReceivedUserId(userId);
        userMessage.setMessageType(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setMessageContent(redisComponent.getSysSetting().getRegisterSetting().getRegisterWelcomeInfo());
        userMessageMapper.insert(userMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral) {
        integral = changeType * integral;
        if (integral == 0) {
            return;
        }
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changeType) && userInfo.getCurrentIntegral() + integral < 0) {
            integral = changeType * userInfo.getCurrentIntegral();
        }
        UserIntegralRecord record = new UserIntegralRecord();
        record.setUserId(userId);
        record.setIntegral(integral);
        record.setCreateTime(new Date());
        record.setOperType(operTypeEnum.getOperType());
        userIntegralRecordMapper.insert(record);

        Integer count = userInfoMapper.updateIntegral(userId, integral);
        if (count == 0) {
            throw new BusinessException("更新用户积分失败");
        }
    }

    @Override
    public SessionWebUserDto login(String email, String password, String ip) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo == null || !password.equals(userInfo.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        String ipAddress = getIpAddress(ip);
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());
        updateInfo.setLastLoginIp(ip);
        updateInfo.setLastLoginIpAddress(ipAddress);
        userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());

        SessionWebUserDto webDto = new SessionWebUserDto();
        webDto.setUserId(userInfo.getUserId());
        webDto.setNickname(userInfo.getNickName());
        webDto.setProvince(ipAddress);
        if (!StringTools.isEmpty(webConfig.getAdminEmail()) && webConfig.getAdminEmail().equals(userInfo.getEmail())) {
            webDto.setAdmin(true);
        } else {
            webDto.setAdmin(false);
        }
        return webDto;
    }

    private final String getIpAddress(String ip) {
        Map<String, String> addressInfo = new HashMap<>();
        try {
            String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip;
            String responseJson = OKHttpUtils.getRequest(url);
            if (StringTools.isEmpty(responseJson)) {
                return Constants.NO_ADDRESS;
            }
            addressInfo = JsonUtils.convertJson2Obj(responseJson, Map.class);
            return addressInfo.get("pro");
        } catch (Exception e) {
            log.error("获取ip所在地失败");
        }
        return Constants.NO_ADDRESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo == null) {
            throw new BusinessException("邮箱不存在");
        }
        emailCodeService.checkCode(email, emailCode);

        UserInfo updateInfo = new UserInfo();
        updateInfo.setPassword(StringTools.encodeByMd5(password));
        userInfoMapper.updateByEmail(updateInfo, email);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo, MultipartFile avatar) {
        userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());
        if (avatar != null) {
            fileUtils.uploadFile2Local(avatar, userInfo.getUserId(), FileUploadTypeEnum.AVATAR);
        }
    }
}