package com.easybbs.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.easybbs.constants.Constants;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.UserInfoMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.EmailCodeQuery;
import com.easybbs.entity.po.EmailCode;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.EmailCodeMapper;
import com.easybbs.service.EmailCodeService;
import com.easybbs.utils.StringTools;


/**
 * 邮箱验证码 业务接口实现
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

	@Resource
	private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private JavaMailSender javaMailSender;

	@Resource
	private WebConfig webConfig;


	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<EmailCode> findListByParam(EmailCodeQuery param) {
		return this.emailCodeMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(EmailCodeQuery param) {
		return this.emailCodeMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<EmailCode> list = this.findListByParam(param);
		PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(EmailCode bean) {
		return this.emailCodeMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<EmailCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.emailCodeMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<EmailCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.emailCodeMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(EmailCode bean, EmailCodeQuery param) {
		StringTools.checkParam(param);
		return this.emailCodeMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(EmailCodeQuery param) {
		StringTools.checkParam(param);
		return this.emailCodeMapper.deleteByParam(param);
	}

	/**
	 * 根据EmailAndCode获取对象
	 */
	@Override
	public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
		return this.emailCodeMapper.selectByEmailAndCode(email, code);
	}

	/**
	 * 根据EmailAndCode修改
	 */
	@Override
	public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
		return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
	}

	/**
	 * 根据EmailAndCode删除
	 */
	@Override
	public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
		return this.emailCodeMapper.deleteByEmailAndCode(email, code);
	}

	@Override
	public void sendEmailCode(String email, Integer type) {
		if(type == Constants.TWO) {
			UserInfo userInfo = userInfoMapper.selectByEmail(email);
			if(userInfo != null) {
				throw new BusinessException("邮箱已存在");
			}
		}
		EmailCode selectByEmail = emailCodeMapper.selectByEmail(email);

		String code = StringTools.getRandomNumber(Constants.LENGTH_5);
		sendEmail(email, code);

		EmailCode emailCode = new EmailCode();
		emailCode.setCode(code);
		emailCode.setEmail(email);
		emailCode.setCreateTime(new Date());
		emailCode.setStatus(Constants.ZERO);

		if(selectByEmail != null) {
			emailCodeMapper.updateByEmail(emailCode);
		}else{
			emailCodeMapper.insert(emailCode);
		}
	}

	public void sendEmail(String email, String code) {
		try{
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setFrom(webConfig.getSendUserName());
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("注册邮箱验证码");
			mimeMessageHelper.setText("邮箱验证码为：" + code);
			mimeMessageHelper.setSentDate(new Date());
			javaMailSender.send(mimeMessage);

		}catch (Exception e) {
			throw new BusinessException("邮件发送失败");
		}
	}

	@Override
	public void checkCode(String email, String code) {
		EmailCode emailCode = emailCodeMapper.selectByEmailAndCode(email, code);
		if(emailCode == null) {
			throw new BusinessException("邮箱验证码错误");
		}
		if(emailCode.getStatus() == 1 || (System.currentTimeMillis() - emailCode.getCreateTime().getTime()) > Constants.LENGTH_15 * 1000 * 60) {
			emailCodeMapper.updateStatusByEmailAndCode(email, code, Constants.ONE);
			throw new BusinessException("邮箱验证码已失效");
		}
		emailCodeMapper.updateStatusByEmailAndCode(email, code, Constants.ONE);
	}
}