package com.easybbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.query.ForumBoardQuery;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.mappers.ForumBoardMapper;
import com.easybbs.service.ForumBoardService;
import com.easybbs.utils.StringTools;


/**
 * 文章板块信息 业务接口实现
 */
@Service("forumBoardService")
public class ForumBoardServiceImpl implements ForumBoardService {

	@Resource
	private ForumBoardMapper<ForumBoard, ForumBoardQuery> forumBoardMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ForumBoard> findListByParam(ForumBoardQuery param) {
		return this.forumBoardMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ForumBoardQuery param) {
		return this.forumBoardMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<ForumBoard> findListByPage(ForumBoardQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumBoard> list = this.findListByParam(param);
		PaginationResultVO<ForumBoard> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumBoard bean) {
		return this.forumBoardMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumBoardMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumBoardMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(ForumBoard bean, ForumBoardQuery param) {
		StringTools.checkParam(param);
		return this.forumBoardMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ForumBoardQuery param) {
		StringTools.checkParam(param);
		return this.forumBoardMapper.deleteByParam(param);
	}

	/**
	 * 根据BoardId获取对象
	 */
	@Override
	public ForumBoard getForumBoardByBoardId(Integer boardId) {
		return this.forumBoardMapper.selectByBoardId(boardId);
	}

	/**
	 * 根据BoardId修改
	 */
	@Override
	public Integer updateForumBoardByBoardId(ForumBoard bean, Integer boardId) {
		return this.forumBoardMapper.updateByBoardId(bean, boardId);
	}

	/**
	 * 根据BoardId删除
	 */
	@Override
	public Integer deleteForumBoardByBoardId(Integer boardId) {
		return this.forumBoardMapper.deleteByBoardId(boardId);
	}
}