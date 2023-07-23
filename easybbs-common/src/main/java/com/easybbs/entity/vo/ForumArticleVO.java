package com.easybbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 文章信息
 */
public class ForumArticleVO implements Serializable {


    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 板块ID
     */
    private Integer boardId;

    /**
     * 板块名称
     */
    private String boardName;

    /**
     * 父级板块ID
     */
    private Integer pBoardId;

    /**
     * 父板块名称
     */
    private String pBoardName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 最后登录ip地址
     */
    private String userIpAddress;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    /**
     * 阅读数量
     */
    private Integer readCount;

    /**
     * 点赞数
     */
    private Integer goodCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 0未置顶  1:已置顶
     */
    private Integer topType;


    /**
     * 0:没有附件  1:有附件
     */
    private Integer attachmentType;

    /**
     * -1已删除 0:待审核  1:已审核
     */
    private Integer status;

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return this.articleId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getBoardId() {
        return this.boardId;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setpBoardId(Integer pBoardId) {
        this.pBoardId = pBoardId;
    }

    public Integer getpBoardId() {
        return this.pBoardId;
    }

    public void setpBoardName(String pBoardName) {
        this.pBoardName = pBoardName;
    }

    public String getpBoardName() {
        return this.pBoardName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public String getUserIpAddress() {
        return this.userIpAddress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return this.cover;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getPostTime() {
        return this.postTime;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getReadCount() {
        return this.readCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getGoodCount() {
        return this.goodCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getCommentCount() {
        return this.commentCount;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public Integer getTopType() {
        return this.topType;
    }

}
