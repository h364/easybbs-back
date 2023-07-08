package com.easybbs.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 文章板块信息 数据库操作接口
 */
public interface ForumBoardMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据BoardId更新
	 */
	 Integer updateByBoardId(@Param("bean") T t,@Param("boardId") Integer boardId);


	/**
	 * 根据BoardId删除
	 */
	 Integer deleteByBoardId(@Param("boardId") Integer boardId);


	/**
	 * 根据BoardId获取对象
	 */
	 T selectByBoardId(@Param("boardId") Integer boardId);


}
