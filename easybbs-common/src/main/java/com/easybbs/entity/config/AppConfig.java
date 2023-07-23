package com.easybbs.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;

    public String getProjectFolder() {
        return projectFolder;
    }
}
