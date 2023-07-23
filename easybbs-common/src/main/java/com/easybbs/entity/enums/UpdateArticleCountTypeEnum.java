package com.easybbs.entity.enums;

public enum UpdateArticleCountTypeEnum {
    READ_COUNT(0, "阅读数"),
    GOOD_COUNT(1, "点赞数"),
    COMMENT_COUNT(2, "评论数");


    private Integer type;
    private String desc;

    UpdateArticleCountTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static UpdateArticleCountTypeEnum getByType(Integer type) {
        for (UpdateArticleCountTypeEnum item : UpdateArticleCountTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
