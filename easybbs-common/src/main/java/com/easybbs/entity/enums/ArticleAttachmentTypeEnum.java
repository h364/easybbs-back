package com.easybbs.entity.enums;

public enum ArticleAttachmentTypeEnum {
    NO(0, "没有附件"),
    YES(1, "有附件");

    private Integer type;
    private String desc;

    ArticleAttachmentTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ArticleAttachmentTypeEnum getByType(Integer type) {
        for (ArticleAttachmentTypeEnum item : ArticleAttachmentTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
