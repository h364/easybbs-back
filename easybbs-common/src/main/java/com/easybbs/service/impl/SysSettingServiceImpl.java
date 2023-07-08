package com.easybbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.SysSettingQuery;
import com.easybbs.entity.po.SysSetting;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.SysSettingMapper;
import com.easybbs.service.SysSettingService;
import com.easybbs.utils.StringTools;


/**
 * 系统设置信息 业务接口实现
 */
@Service("sysSettingService")
public class SysSettingServiceImpl implements SysSettingService {

	@Resource
	private SysSettingMapper<SysSetting, SysSettingQuery> sysSettingMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<SysSetting> findListByParam(SysSettingQuery param) {
		return this.sysSettingMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SysSettingQuery param) {
		return this.sysSettingMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<SysSetting> findListByPage(SysSettingQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<SysSetting> list = this.findListByParam(param);
		PaginationResultVO<SysSetting> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(SysSetting bean) {
		return this.sysSettingMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<SysSetting> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysSettingMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<SysSetting> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysSettingMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(SysSetting bean, SysSettingQuery param) {
		StringTools.checkParam(param);
		return this.sysSettingMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SysSettingQuery param) {
		StringTools.checkParam(param);
		return this.sysSettingMapper.deleteByParam(param);
	}

	/**
	 * 根据Code获取对象
	 */
	@Override
	public SysSetting getSysSettingByCode(String code) {
		return this.sysSettingMapper.selectByCode(code);
	}

	/**
	 * 根据Code修改
	 */
	@Override
	public Integer updateSysSettingByCode(SysSetting bean, String code) {
		return this.sysSettingMapper.updateByCode(bean, code);
	}

	/**
	 * 根据Code删除
	 */
	@Override
	public Integer deleteSysSettingByCode(String code) {
		return this.sysSettingMapper.deleteByCode(code);
	}
}