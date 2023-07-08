package com.easybbs.entity.query;



/**
 * 用户附件下载参数
 */
public class ForumArticleAttachmentDownloadQuery extends BaseParam {


	/**
	 * 文件ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
	 * 用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
	 * 文件下载次数
	 */
	private Integer downloadCount;


	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public String getFileId(){
		return this.fileId;
	}

	public void setFileIdFuzzy(String fileIdFuzzy){
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy(){
		return this.fileIdFuzzy;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
	}

	public void setArticleId(String articleId){
		this.articleId = articleId;
	}

	public String getArticleId(){
		return this.articleId;
	}

	public void setArticleIdFuzzy(String articleIdFuzzy){
		this.articleIdFuzzy = articleIdFuzzy;
	}

	public String getArticleIdFuzzy(){
		return this.articleIdFuzzy;
	}

	public void setDownloadCount(Integer downloadCount){
		this.downloadCount = downloadCount;
	}

	public Integer getDownloadCount(){
		return this.downloadCount;
	}

}
