package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 用户消息
 */
public class UserMessage implements Serializable {


	/**
	 * 自增ID
	 */
	private Integer messageId;

	/**
	 * 接收人用户ID
	 */
	private String receivedUserId;

	/**
	 * 文章ID
	 */
	private String articleId;

	/**
	 * 文章标题
	 */
	private String articleTitle;

	/**
	 * 评论ID
	 */
	private Integer commentId;

	/**
	 * 发送人用户ID
	 */
	private String sendUserId;

	/**
	 * 发送人昵称
	 */
	private String sendNickName;

	/**
	 * 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
	 */
	private Integer messageType;

	/**
	 * 消息内容
	 */
	private String messageContent;

	/**
	 * 1:未读 2:已读
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;


	public void setMessageId(Integer messageId){
		this.messageId = messageId;
	}

	public Integer getMessageId(){
		return this.messageId;
	}

	public void setReceivedUserId(String receivedUserId){
		this.receivedUserId = receivedUserId;
	}

	public String getReceivedUserId(){
		return this.receivedUserId;
	}

	public void setArticleId(String articleId){
		this.articleId = articleId;
	}

	public String getArticleId(){
		return this.articleId;
	}

	public void setArticleTitle(String articleTitle){
		this.articleTitle = articleTitle;
	}

	public String getArticleTitle(){
		return this.articleTitle;
	}

	public void setCommentId(Integer commentId){
		this.commentId = commentId;
	}

	public Integer getCommentId(){
		return this.commentId;
	}

	public void setSendUserId(String sendUserId){
		this.sendUserId = sendUserId;
	}

	public String getSendUserId(){
		return this.sendUserId;
	}

	public void setSendNickName(String sendNickName){
		this.sendNickName = sendNickName;
	}

	public String getSendNickName(){
		return this.sendNickName;
	}

	public void setMessageType(Integer messageType){
		this.messageType = messageType;
	}

	public Integer getMessageType(){
		return this.messageType;
	}

	public void setMessageContent(String messageContent){
		this.messageContent = messageContent;
	}

	public String getMessageContent(){
		return this.messageContent;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	@Override
	public String toString (){
		return "自增ID:"+(messageId == null ? "空" : messageId)+"，接收人用户ID:"+(receivedUserId == null ? "空" : receivedUserId)+"，文章ID:"+(articleId == null ? "空" : articleId)+"，文章标题:"+(articleTitle == null ? "空" : articleTitle)+"，评论ID:"+(commentId == null ? "空" : commentId)+"，发送人用户ID:"+(sendUserId == null ? "空" : sendUserId)+"，发送人昵称:"+(sendNickName == null ? "空" : sendNickName)+"，0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载:"+(messageType == null ? "空" : messageType)+"，消息内容:"+(messageContent == null ? "空" : messageContent)+"，1:未读 2:已读:"+(status == null ? "空" : status)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
