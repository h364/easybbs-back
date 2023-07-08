package com.easybbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.UserIntegralRecordQuery;
import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.UserIntegralRecordMapper;
import com.easybbs.service.UserIntegralRecordService;
import com.easybbs.utils.StringTools;


/**
 * 用户积分记录表 业务接口实现
 */
@Service("userIntegralRecordService")
public class UserIntegralRecordServiceImpl implements UserIntegralRecordService {

	@Resource
	private UserIntegralRecordMapper<UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserIntegralRecord> findListByParam(UserIntegralRecordQuery param) {
		return this.userIntegralRecordMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserIntegralRecordQuery param) {
		return this.userIntegralRecordMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserIntegralRecord> findListByPage(UserIntegralRecordQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserIntegralRecord> list = this.findListByParam(param);
		PaginationResultVO<UserIntegralRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserIntegralRecord bean) {
		return this.userIntegralRecordMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserIntegralRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userIntegralRecordMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserIntegralRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userIntegralRecordMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserIntegralRecord bean, UserIntegralRecordQuery param) {
		StringTools.checkParam(param);
		return this.userIntegralRecordMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserIntegralRecordQuery param) {
		StringTools.checkParam(param);
		return this.userIntegralRecordMapper.deleteByParam(param);
	}

	/**
	 * 根据RecordId获取对象
	 */
	@Override
	public UserIntegralRecord getUserIntegralRecordByRecordId(Integer recordId) {
		return this.userIntegralRecordMapper.selectByRecordId(recordId);
	}

	/**
	 * 根据RecordId修改
	 */
	@Override
	public Integer updateUserIntegralRecordByRecordId(UserIntegralRecord bean, Integer recordId) {
		return this.userIntegralRecordMapper.updateByRecordId(bean, recordId);
	}

	/**
	 * 根据RecordId删除
	 */
	@Override
	public Integer deleteUserIntegralRecordByRecordId(Integer recordId) {
		return this.userIntegralRecordMapper.deleteByRecordId(recordId);
	}
}