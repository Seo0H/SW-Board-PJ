package com.board;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/profile/**").addResourceLocations("file:///d:/Repository/profile/");
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
				.setCachePeriod(60 * 60 * 24 * 365);

		registry.addResourceHandler("/member/css/**").addResourceLocations("classpath:/static/css/")
				.setCachePeriod(60 * 60 * 24 * 365);
		registry.addResourceHandler("/userManage/css/**").addResourceLocations("classpath:/static/css/")
		.setCachePeriod(60 * 60 * 24 * 365);
		registry.addResourceHandler("/board/css/**").addResourceLocations("classpath:/static/css/")
		.setCachePeriod(60 * 60 * 24 * 365);

		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/")
				.setCachePeriod(60 * 60 * 24 * 365);
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/")
		.setCachePeriod(60 * 60 * 24 * 365);

		registry.addResourceHandler("/member/fonts/**").addResourceLocations("classpath:/static/fonts/")
				.setCachePeriod(60 * 60 * 24 * 365);
		registry.addResourceHandler("/userManage/fonts/**").addResourceLocations("classpath:/static/fonts/")
		.setCachePeriod(60 * 60 * 24 * 365);
		registry.addResourceHandler("/board/fonts/**").addResourceLocations("classpath:/static/fonts/")
		.setCachePeriod(60 * 60 * 24 * 365);
	}
}
