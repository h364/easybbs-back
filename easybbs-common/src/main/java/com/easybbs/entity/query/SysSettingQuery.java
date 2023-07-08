package com.easybbs.entity.query;



/**
 * 系统设置信息参数
 */
public class SysSettingQuery extends BaseParam {


	/**
	 * 编号
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * 设置信息
	 */
	private String jsonContent;

	private String jsonContentFuzzy;


	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setCodeFuzzy(String codeFuzzy){
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy(){
		return this.codeFuzzy;
	}

	public void setJsonContent(String jsonContent){
		this.jsonContent = jsonContent;
	}

	public String getJsonContent(){
		return this.jsonContent;
	}

	public void setJsonContentFuzzy(String jsonContentFuzzy){
		this.jsonContentFuzzy = jsonContentFuzzy;
	}

	public String getJsonContentFuzzy(){
		return this.jsonContentFuzzy;
	}

}
