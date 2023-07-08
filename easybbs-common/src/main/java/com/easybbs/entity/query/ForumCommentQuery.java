package com.easybbs.entity.query;

import java.util.Date;


/**
 * 评论参数
 */
public class ForumCommentQuery extends BaseParam {


	/**
	 * 评论ID
	 */
	private Integer commentId;

	/**
	 * 父级评论ID
	 */
	private Integer pCommentId;

	/**
	 * 文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
	 * 回复内容
	 */
	private String content;

	private String contentFuzzy;

	/**
	 * 图片
	 */
	private String imgPath;

	private String imgPathFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 用户ip地址
	 */
	private String userIpAddress;

	private String userIpAddressFuzzy;

	/**
	 * 回复人ID
	 */
	private String replyUserId;

	private String replyUserIdFuzzy;

	/**
	 * 回复人昵称
	 */
	private String replyNickName;

	private String replyNickNameFuzzy;

	/**
	 * 0:未置顶  1:置顶
	 */
	private Integer topType;

	/**
	 * 发布时间
	 */
	private String postTime;

	private String postTimeStart;

	private String postTimeEnd;

	/**
	 * good数量
	 */
	private Integer goodCount;

	/**
	 * 0:待审核  1:已审核
	 */
	private Integer status;


	public void setCommentId(Integer commentId){
		this.commentId = commentId;
	}

	public Integer getCommentId(){
		return this.commentId;
	}

	public void setpCommentId(Integer pCommentId){
		this.pCommentId = pCommentId;
	}

	public Integer getpCommentId(){
		return this.pCommentId;
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

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setContentFuzzy(String contentFuzzy){
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy(){
		return this.contentFuzzy;
	}

	public void setImgPath(String imgPath){
		this.imgPath = imgPath;
	}

	public String getImgPath(){
		return this.imgPath;
	}

	public void setImgPathFuzzy(String imgPathFuzzy){
		this.imgPathFuzzy = imgPathFuzzy;
	}

	public String getImgPathFuzzy(){
		return this.imgPathFuzzy;
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

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setNickNameFuzzy(String nickNameFuzzy){
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy(){
		return this.nickNameFuzzy;
	}

	public void setUserIpAddress(String userIpAddress){
		this.userIpAddress = userIpAddress;
	}

	public String getUserIpAddress(){
		return this.userIpAddress;
	}

	public void setUserIpAddressFuzzy(String userIpAddressFuzzy){
		this.userIpAddressFuzzy = userIpAddressFuzzy;
	}

	public String getUserIpAddressFuzzy(){
		return this.userIpAddressFuzzy;
	}

	public void setReplyUserId(String replyUserId){
		this.replyUserId = replyUserId;
	}

	public String getReplyUserId(){
		return this.replyUserId;
	}

	public void setReplyUserIdFuzzy(String replyUserIdFuzzy){
		this.replyUserIdFuzzy = replyUserIdFuzzy;
	}

	public String getReplyUserIdFuzzy(){
		return this.replyUserIdFuzzy;
	}

	public void setReplyNickName(String replyNickName){
		this.replyNickName = replyNickName;
	}

	public String getReplyNickName(){
		return this.replyNickName;
	}

	public void setReplyNickNameFuzzy(String replyNickNameFuzzy){
		this.replyNickNameFuzzy = replyNickNameFuzzy;
	}

	public String getReplyNickNameFuzzy(){
		return this.replyNickNameFuzzy;
	}

	public void setTopType(Integer topType){
		this.topType = topType;
	}

	public Integer getTopType(){
		return this.topType;
	}

	public void setPostTime(String postTime){
		this.postTime = postTime;
	}

	public String getPostTime(){
		return this.postTime;
	}

	public void setPostTimeStart(String postTimeStart){
		this.postTimeStart = postTimeStart;
	}

	public String getPostTimeStart(){
		return this.postTimeStart;
	}
	public void setPostTimeEnd(String postTimeEnd){
		this.postTimeEnd = postTimeEnd;
	}

	public String getPostTimeEnd(){
		return this.postTimeEnd;
	}

	public void setGoodCount(Integer goodCount){
		this.goodCount = goodCount;
	}

	public Integer getGoodCount(){
		return this.goodCount;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}
