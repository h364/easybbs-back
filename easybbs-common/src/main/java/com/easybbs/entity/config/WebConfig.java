package com.easybbs.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebConfig extends AppConfig {

    @Value("${spring.mail.username:}")
    private String SendUserName;

    public String getSendUserName() {
        return SendUserName;
    }
}
