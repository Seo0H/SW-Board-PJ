package com.board;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

    	registry.addResourceHandler("/profile/**")
				.addResourceLocations("file:///D:/Repository/profile/");
    	registry.addResourceHandler("/**")
    			.addResourceLocations("classpath:/static/");
    }

}
