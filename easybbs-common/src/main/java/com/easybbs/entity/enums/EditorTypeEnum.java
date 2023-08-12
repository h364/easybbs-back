package com.easybbs.entity.enums;

public enum EditorTypeEnum {
    RICH(0, "富文本"),
    MARKDOWN(1, "Markdown");


    private Integer type;
    private String desc;

    EditorTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static EditorTypeEnum getByType(Integer type) {
        for (EditorTypeEnum item : EditorTypeEnum.values()) {
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
