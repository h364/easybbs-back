package com.easybbs.entity.enums;

public enum VerifyRegexEnum {
    NO("", "不校验"),
    EMAIL("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", "邮箱"),
    PASSWORD("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,16}$", "密码由八-十六位组成,至少包含数字大写和小写字母");

    private String regex;
    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {
        return regex;
    }

    public String getDesc() {
        return desc;
    }
}
