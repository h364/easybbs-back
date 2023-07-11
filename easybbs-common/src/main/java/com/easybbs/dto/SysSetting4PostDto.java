package com.easybbs.dto;

import com.easybbs.anotation.VerifyParam;

public class SysSetting4PostDto {

    @VerifyParam(required = true)
    private Integer postIntegral;

    @VerifyParam(required = true)
    private Integer postDayCountThreshold;

    @VerifyParam(required = true)
    private Integer dayImageUploadCount;

    @VerifyParam(required = true)
    private Integer attachmentSize;

    public Integer getPostIntegral() {
        return postIntegral;
    }

    public void setPostIntegral(Integer postIntegral) {
        this.postIntegral = postIntegral;
    }

    public Integer getPostDayCountThreshold() {
        return postDayCountThreshold;
    }

    public void setPostDayCountThreshold(Integer postDayCountThreshold) {
        this.postDayCountThreshold = postDayCountThreshold;
    }

    public Integer getDayImageUploadCount() {
        return dayImageUploadCount;
    }

    public void setDayImageUploadCount(Integer dayImageUploadCount) {
        this.dayImageUploadCount = dayImageUploadCount;
    }

    public Integer getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(Integer attachmentSize) {
        this.attachmentSize = attachmentSize;
    }
}
