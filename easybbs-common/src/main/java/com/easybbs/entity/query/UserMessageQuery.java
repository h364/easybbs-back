package com.easybbs.entity.query;

import java.util.Date;


/**
 * 用户消息参数
 */
public class UserMessageQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer messageId;

	/**
	 * 接收人用户ID
	 */
	private String receivedUserId;

	private String receivedUserIdFuzzy;

	/**
	 * 文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
	 * 文章标题
	 */
	private String articleTitle;

	private String articleTitleFuzzy;

	/**
	 * 评论ID
	 */
	private Integer commentId;

	/**
	 * 发送人用户ID
	 */
	private String sendUserId;

	private String sendUserIdFuzzy;

	/**
	 * 发送人昵称
	 */
	private String sendNickName;

	private String sendNickNameFuzzy;

	/**
	 * 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
	 */
	private Integer messageType;

	/**
	 * 消息内容
	 */
	private String messageContent;

	private String messageContentFuzzy;

	/**
	 * 1:未读 2:已读
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;


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

	public void setReceivedUserIdFuzzy(String receivedUserIdFuzzy){
		this.receivedUserIdFuzzy = receivedUserIdFuzzy;
	}

	public String getReceivedUserIdFuzzy(){
		return this.receivedUserIdFuzzy;
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

	public void setArticleTitle(String articleTitle){
		this.articleTitle = articleTitle;
	}

	public String getArticleTitle(){
		return this.articleTitle;
	}

	public void setArticleTitleFuzzy(String articleTitleFuzzy){
		this.articleTitleFuzzy = articleTitleFuzzy;
	}

	public String getArticleTitleFuzzy(){
		return this.articleTitleFuzzy;
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

	public void setSendUserIdFuzzy(String sendUserIdFuzzy){
		this.sendUserIdFuzzy = sendUserIdFuzzy;
	}

	public String getSendUserIdFuzzy(){
		return this.sendUserIdFuzzy;
	}

	public void setSendNickName(String sendNickName){
		this.sendNickName = sendNickName;
	}

	public String getSendNickName(){
		return this.sendNickName;
	}

	public void setSendNickNameFuzzy(String sendNickNameFuzzy){
		this.sendNickNameFuzzy = sendNickNameFuzzy;
	}

	public String getSendNickNameFuzzy(){
		return this.sendNickNameFuzzy;
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

	public void setMessageContentFuzzy(String messageContentFuzzy){
		this.messageContentFuzzy = messageContentFuzzy;
	}

	public String getMessageContentFuzzy(){
		return this.messageContentFuzzy;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return this.createTime;
	}

	public void setCreateTimeStart(String createTimeStart){
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart(){
		return this.createTimeStart;
	}
	public void setCreateTimeEnd(String createTimeEnd){
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd(){
		return this.createTimeEnd;
	}

}
