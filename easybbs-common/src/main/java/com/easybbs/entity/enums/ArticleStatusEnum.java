package com.easybbs.entity.enums;

public enum ArticleStatusEnum {
    DEL(-1, "已删除"),
    NO_AUDIT(0, "待审核"),
    AUDIT(1, "已审核");


    private Integer status;
    private String desc;

    ArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ArticleStatusEnum getByStatus(Integer status) {
        for (ArticleStatusEnum item : ArticleStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
