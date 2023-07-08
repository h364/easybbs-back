package com.easybbs.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 点赞记录 数据库操作接口
 */
public interface LikeRecordMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据OpId更新
	 */
	 Integer updateByOpId(@Param("bean") T t,@Param("opId") Integer opId);


	/**
	 * 根据OpId删除
	 */
	 Integer deleteByOpId(@Param("opId") Integer opId);


	/**
	 * 根据OpId获取对象
	 */
	 T selectByOpId(@Param("opId") Integer opId);


	/**
	 * 根据ObjectIdAndUserIdAndOpType更新
	 */
	 Integer updateByObjectIdAndUserIdAndOpType(@Param("bean") T t,@Param("objectId") String objectId,@Param("userId") String userId,@Param("opType") Integer opType);


	/**
	 * 根据ObjectIdAndUserIdAndOpType删除
	 */
	 Integer deleteByObjectIdAndUserIdAndOpType(@Param("objectId") String objectId,@Param("userId") String userId,@Param("opType") Integer opType);


	/**
	 * 根据ObjectIdAndUserIdAndOpType获取对象
	 */
	 T selectByObjectIdAndUserIdAndOpType(@Param("objectId") String objectId,@Param("userId") String userId,@Param("opType") Integer opType);


}
