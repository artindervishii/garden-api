package com.garden.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = System.getProperty("user.dir") + "/uploads/categories/";
        
        registry.addResourceHandler("/images/categories/**")
                .addResourceLocations(
                        "file:" + uploadPath,
                        "file:" + uploadPath + "original/",
                        "file:" + uploadPath + "optimized/",
                        "file:" + uploadPath + "optimized/webp/",
                        "file:" + uploadPath + "optimized/*/",
                        "file:" + uploadPath + "optimized/webp/*/"
                )
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic());

        String projectImagesPath = System.getProperty("user.dir") + "/uploads/projects/images/";
        registry.addResourceHandler("/images/projects/**")
                .addResourceLocations(
                        "file:" + projectImagesPath,
                        "file:" + projectImagesPath + "original/",
                        "file:" + projectImagesPath + "optimized/",
                        "file:" + projectImagesPath + "optimized/webp/",
                        "file:" + projectImagesPath + "optimized/*/",
                        "file:" + projectImagesPath + "optimized/webp/*/"
                )
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/videos/projects/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/projects/videos/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
    }
}


