package com.apartment.www.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfig {

    @Value("${spring.security.user.name}")
    private String adminUsername;


    @Value("${spring.security.user.password}")
    private String adminPassword;
}
