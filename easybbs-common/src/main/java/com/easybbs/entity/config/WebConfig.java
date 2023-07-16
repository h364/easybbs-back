package com.easybbs.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebConfig extends AppConfig {

    @Value("${spring.mail.username:}")
    private String SendUserName;

    @Value("${admin.mail:}")
    private String adminEmail;

    @Value("${admin.account:}")
    private String adminAccount;

    @Value("${admin.password:}")
    private String adminPassword;

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getSendUserName() {
        return SendUserName;
    }
}
