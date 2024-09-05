package com.projects.symbolRecognition.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfo {
    private static String appName;

    @Autowired
    public ApplicationInfo(Environment environment) {
        appName = environment.getProperty("application.name");
    }

    public static String getApplicationName() {
        return appName;
    }
}
