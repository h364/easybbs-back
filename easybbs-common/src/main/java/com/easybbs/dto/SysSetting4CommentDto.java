package com.easybbs.dto;

public class SysSetting4CommentDto {

    //评论积分
    private Integer commentIntegral;
    //每天评论数量的限制
    private Integer commentDayCountThreshold;
    //是否开启评论
    private Boolean commentOpen;

    public Integer getCommentIntegral() {
        return commentIntegral;
    }

    public void setCommentIntegral(Integer commentIntegral) {
        this.commentIntegral = commentIntegral;
    }

    public Integer getCommentDayCountThreshold() {
        return commentDayCountThreshold;
    }

    public void setCommentDayCountThreshold(Integer commentDayCountThreshold) {
        this.commentDayCountThreshold = commentDayCountThreshold;
    }

    public Boolean getCommentOpen() {
        return commentOpen;
    }

    public void setCommentOpen(Boolean commentOpen) {
        this.commentOpen = commentOpen;
    }
}
