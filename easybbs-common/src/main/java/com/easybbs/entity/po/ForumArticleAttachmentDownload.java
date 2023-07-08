package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 用户附件下载
 */
public class ForumArticleAttachmentDownload implements Serializable {


	/**
	 * 文件ID
	 */
	private String fileId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 文章ID
	 */
	private String articleId;

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

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setArticleId(String articleId){
		this.articleId = articleId;
	}

	public String getArticleId(){
		return this.articleId;
	}

	public void setDownloadCount(Integer downloadCount){
		this.downloadCount = downloadCount;
	}

	public Integer getDownloadCount(){
		return this.downloadCount;
	}

	@Override
	public String toString (){
		return "文件ID:"+(fileId == null ? "空" : fileId)+"，用户id:"+(userId == null ? "空" : userId)+"，文章ID:"+(articleId == null ? "空" : articleId)+"，文件下载次数:"+(downloadCount == null ? "空" : downloadCount);
	}
}
