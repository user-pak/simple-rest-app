package com.example.demo.greeting;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer{

	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/chatchat").setViewName("chatchat");
		registry.addViewController("/views").setViewName("views");
		registry.addViewController("/pictures").setViewName("pictures");

	}
}
