package com.easybbs.dto;

import com.easybbs.anotation.VerifyParam;

public class SysSetting4EmailDto {

    @VerifyParam(required = true)
    private String emailTitle;

    @VerifyParam(required = true)
    private String emailContent;

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}
