package com.mars.framework.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "system.shiro")
@Data
public class ShiroProperties {

    private String login;
    private String logout;

}
