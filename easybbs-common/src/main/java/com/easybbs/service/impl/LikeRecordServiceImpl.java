package com.easybbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.LikeRecordQuery;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.LikeRecordMapper;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.StringTools;


/**
 * 点赞记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

	@Resource
	private LikeRecordMapper<LikeRecord, LikeRecordQuery> likeRecordMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<LikeRecord> findListByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<LikeRecord> list = this.findListByParam(param);
		PaginationResultVO<LikeRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(LikeRecord bean) {
		return this.likeRecordMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(LikeRecord bean, LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.deleteByParam(param);
	}

	/**
	 * 根据OpId获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.selectByOpId(opId);
	}

	/**
	 * 根据OpId修改
	 */
	@Override
	public Integer updateLikeRecordByOpId(LikeRecord bean, Integer opId) {
		return this.likeRecordMapper.updateByOpId(bean, opId);
	}

	/**
	 * 根据OpId删除
	 */
	@Override
	public Integer deleteLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.deleteByOpId(opId);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType修改
	 */
	@Override
	public Integer updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType删除
	 */
	@Override
	public Integer deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}
}