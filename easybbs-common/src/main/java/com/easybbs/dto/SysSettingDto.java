package com.easybbs.dto;

public class SysSettingDto {

    private SysSetting4AuditDto auditSetting;

    private SysSetting4CommentDto commentSetting;

    private SysSetting4EmailDto emailSetting;

    private SysSetting4LikeDto likeSetting;

    private SysSetting4PostDto postSetting;

    private SysSetting4RegisterDto registerSetting;

    public SysSetting4AuditDto getAuditSetting() {
        return auditSetting;
    }

    public void setAuditSetting(SysSetting4AuditDto auditSetting) {
        this.auditSetting = auditSetting;
    }

    public SysSetting4CommentDto getCommentSetting() {
        return commentSetting;
    }

    public void setCommentSetting(SysSetting4CommentDto commentSetting) {
        this.commentSetting = commentSetting;
    }

    public SysSetting4EmailDto getEmailSetting() {
        return emailSetting;
    }

    public void setEmailSetting(SysSetting4EmailDto emailSetting) {
        this.emailSetting = emailSetting;
    }

    public SysSetting4LikeDto getLikeSetting() {
        return likeSetting;
    }

    public void setLikeSetting(SysSetting4LikeDto likeSetting) {
        this.likeSetting = likeSetting;
    }

    public SysSetting4PostDto getPostSetting() {
        return postSetting;
    }

    public void setPostSetting(SysSetting4PostDto postSetting) {
        this.postSetting = postSetting;
    }

    public SysSetting4RegisterDto getRegisterSetting() {
        return registerSetting;
    }

    public void setRegisterSetting(SysSetting4RegisterDto registerSetting) {
        this.registerSetting = registerSetting;
    }
}
