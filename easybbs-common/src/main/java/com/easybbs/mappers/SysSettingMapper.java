package com.easybbs.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 系统设置信息 数据库操作接口
 */
public interface SysSettingMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据Code更新
	 */
	 Integer updateByCode(@Param("bean") T t,@Param("code") String code);


	/**
	 * 根据Code删除
	 */
	 Integer deleteByCode(@Param("code") String code);


	/**
	 * 根据Code获取对象
	 */
	 T selectByCode(@Param("code") String code);


}
