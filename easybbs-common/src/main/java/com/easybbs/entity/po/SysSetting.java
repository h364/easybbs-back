package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 系统设置信息
 */
public class SysSetting implements Serializable {


	/**
	 * 编号
	 */
	private String code;

	/**
	 * 设置信息
	 */
	private String jsonContent;


	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setJsonContent(String jsonContent){
		this.jsonContent = jsonContent;
	}

	public String getJsonContent(){
		return this.jsonContent;
	}

	@Override
	public String toString (){
		return "编号:"+(code == null ? "空" : code)+"，设置信息:"+(jsonContent == null ? "空" : jsonContent);
	}
}
