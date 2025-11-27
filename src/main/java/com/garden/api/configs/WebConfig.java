package com.garden.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = System.getProperty("user.dir") + "/uploads/categories/";
        registry.addResourceHandler("/images/categories/**")
                .addResourceLocations("file:" + uploadPath);

        registry.addResourceHandler("/images/projects/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/projects/images/");

        registry.addResourceHandler("/videos/projects/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/projects/videos/");
    }
}


