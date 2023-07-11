package com.easybbs.dto;

import com.easybbs.anotation.VerifyParam;

public class SysSetting4AuditDto {

    @VerifyParam(required = true)
    private Boolean postAudit;

    @VerifyParam(required = true)
    private Boolean commentAudit;

    public Boolean getPostAudit() {
        return postAudit;
    }

    public void setPostAudit(Boolean postAudit) {
        this.postAudit = postAudit;
    }

    public Boolean getCommentAudit() {
        return commentAudit;
    }

    public void setCommentAudit(Boolean commentAudit) {
        this.commentAudit = commentAudit;
    }
}
