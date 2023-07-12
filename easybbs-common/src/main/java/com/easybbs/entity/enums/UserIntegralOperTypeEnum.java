package com.easybbs.entity.enums;

public enum UserIntegralOperTypeEnum {
    REGISTER(1, "账号注册"),
    DOWNLOAD_ATTACHMENT(2, "下载附件"),
    USER_DOWNLOAD_ATTACHMENT(3, "下载附件"),
    POST_COMMENT(4, "发布评论"),
    POST_ARTICLE(5, "发布文章"),
    ADMIN(6, "管理员操作"),
    DEL_ARTICLE(7, "文章被删除"),
    DEL_COMMENT(8, "评论被删除");

    public Integer getOperType() {
        return OperType;
    }

    public String getDesc() {
        return desc;
    }

    private Integer OperType;
    private String desc;

    UserIntegralOperTypeEnum(Integer operType, String desc) {
        OperType = operType;
        this.desc = desc;
    }

    public static UserIntegralOperTypeEnum getByType(Integer operType) {
        for (UserIntegralOperTypeEnum typeEnum : UserIntegralOperTypeEnum.values()) {
            if (typeEnum.getOperType().equals(operType)) {
                return typeEnum;
            }
        }
        return null;
    }
}
