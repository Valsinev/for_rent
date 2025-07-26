package com.apartment.www.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesConfig implements WebMvcConfigurer {


	@Value("${photos.folder}")
	private String photosFolder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/external-images/**")
				.addResourceLocations(photosFolder);
	}

	@PostConstruct
	public void logPath() {
		System.out.println("Photos folder mapped to: " + photosFolder);
	}
}
