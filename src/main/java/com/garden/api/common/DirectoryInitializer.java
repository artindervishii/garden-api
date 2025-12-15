package com.garden.api.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Initializes all required upload directories on application startup.
 * This ensures that all necessary folders exist when the application is deployed,
 * even if they are not present in the repository.
 */
@Component
@Order(0) // Run before other initializers
public class DirectoryInitializer implements CommandLineRunner {

    private static final int[] RESPONSIVE_SIZES = {320, 640, 768, 1024, 1280, 1920, 2560};
    private static final String BASE_UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Override
    public void run(String... args) {
        System.out.println("Initializing upload directories...");
        
        try {
            // Create categories directories
            createCategoryDirectories();
            
            // Create project directories
            createProjectDirectories();
            
            System.out.println("Upload directories initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error initializing upload directories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createCategoryDirectories() {
        String categoriesDir = BASE_UPLOAD_DIR + "categories/";
        createDirectory(categoriesDir);
        
        // Original images
        createDirectory(categoriesDir + "original/");
        
        // Optimized images
        String optimizedDir = categoriesDir + "optimized/";
        createDirectory(optimizedDir);
        
        // WebP directory
        String webpDir = optimizedDir + "webp/";
        createDirectory(webpDir);
        
        // Create size-specific directories for optimized images
        for (int size : RESPONSIVE_SIZES) {
            createDirectory(optimizedDir + size + "w/");
            createDirectory(webpDir + size + "w/");
        }
        
        System.out.println("Category directories created.");
    }

    private void createProjectDirectories() {
        String projectsDir = BASE_UPLOAD_DIR + "projects/";
        createDirectory(projectsDir);
        
        // Project images
        String imagesDir = projectsDir + "images/";
        createDirectory(imagesDir);
        
        // Original images
        createDirectory(imagesDir + "original/");
        
        // Optimized images
        String optimizedDir = imagesDir + "optimized/";
        createDirectory(optimizedDir);
        
        // WebP directory
        String webpDir = optimizedDir + "webp/";
        createDirectory(webpDir);
        
        // Create size-specific directories for optimized images
        for (int size : RESPONSIVE_SIZES) {
            createDirectory(optimizedDir + size + "w/");
            createDirectory(webpDir + size + "w/");
        }
        
        // Project videos
        createDirectory(projectsDir + "videos/");
        
        // Project photos (if used)
        createDirectory(projectsDir + "photos/");
        
        System.out.println("Project directories created.");
    }

    private void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + path);
            } else {
                System.err.println("Failed to create directory: " + path);
            }
        }
    }
}

