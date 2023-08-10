package com.easybbs.entity.enums;

public enum CommentStatusEnum {
    DEL(-1, "已删除"),
    NO_AUDIT(0, "待审核"),
    AUDIT(1, "已审核");


    private Integer status;
    private String desc;

    CommentStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CommentStatusEnum getByStatus(Integer status) {
        for (CommentStatusEnum item : CommentStatusEnum.values()) {
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
