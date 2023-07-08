package com.easybbs.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 用户积分记录表 数据库操作接口
 */
public interface UserIntegralRecordMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据RecordId更新
	 */
	 Integer updateByRecordId(@Param("bean") T t,@Param("recordId") Integer recordId);


	/**
	 * 根据RecordId删除
	 */
	 Integer deleteByRecordId(@Param("recordId") Integer recordId);


	/**
	 * 根据RecordId获取对象
	 */
	 T selectByRecordId(@Param("recordId") Integer recordId);


}
