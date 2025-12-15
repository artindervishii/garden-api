package com.garden.api.common;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ImageOptimizationService {
    
    private static final boolean WEBP_SUPPORTED;
    
    static {
        boolean webpSupported = false;
        try {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            webpSupported = writers.hasNext();
            if (!webpSupported) {
                try {
                    Class<?> readerSpi = Class.forName("org.sejda.imageio.webp.WebPImageReaderSpi");
                    Class<?> writerSpi = Class.forName("org.sejda.imageio.webp.WebPImageWriterSpi");
                    javax.imageio.spi.IIORegistry registry = javax.imageio.spi.IIORegistry.getDefaultInstance();
                    registry.registerServiceProvider((javax.imageio.spi.ImageReaderSpi) readerSpi.getDeclaredConstructor().newInstance());
                    registry.registerServiceProvider((javax.imageio.spi.ImageWriterSpi) writerSpi.getDeclaredConstructor().newInstance());
                    webpSupported = true;
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        WEBP_SUPPORTED = webpSupported;
    }

    private static final int MAX_WIDTH = 3840;
    private static final int MAX_HEIGHT = 2160;
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    private static final float JPEG_QUALITY = 0.85f;
    private static final float WEBP_QUALITY = 0.85f;
    
    private static final int[] RESPONSIVE_SIZES = {320, 640, 768, 1024, 1280, 1920, 2560};
    
    /**
     * Optimize image: resize, compress, and generate multiple formats
     * @param file Original image file
     * @param uploadDir Directory to save optimized images
     * @param baseFilename Base filename (without extension)
     * @param imageType Type of image: "projects" or "categories"
     * @return Map containing URLs for different formats and sizes
     */
    public Map<String, String> optimizeImage(MultipartFile file, String uploadDir, String baseFilename, String imageType) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image file size exceeds maximum limit of 5MB");
        }
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String optimizedDir = uploadDir + "optimized/";
        String webpDir = uploadDir + "optimized/webp/";
        String originalDir = uploadDir + "original/";
        
        new File(optimizedDir).mkdirs();
        new File(webpDir).mkdirs();
        new File(originalDir).mkdirs();
        
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Unable to read image file");
        }
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            double scale = Math.min((double) MAX_WIDTH / originalWidth, (double) MAX_HEIGHT / originalHeight);
            originalImage = Thumbnails.of(originalImage)
                    .scale(scale)
                    .asBufferedImage();
            originalWidth = originalImage.getWidth();
            originalHeight = originalImage.getHeight();
        }
        String originalOptimizedPath = originalDir + baseFilename + ".jpg";
        saveOptimizedImage(originalImage, originalOptimizedPath, "jpg", JPEG_QUALITY);
        
        // Generate WebP version only if supported
        if (WEBP_SUPPORTED) {
            try {
                String webpPath = webpDir + baseFilename + ".webp";
                saveOptimizedImage(originalImage, webpPath, "webp", WEBP_QUALITY);
            } catch (Exception e) {
                System.err.println("Warning: Failed to generate WebP image: " + e.getMessage());
            }
        }
        
        Map<String, String> responsiveUrls = new HashMap<>();
        for (int width : RESPONSIVE_SIZES) {
            if (width <= originalWidth) {
                BufferedImage resized = Thumbnails.of(originalImage)
                        .width(width)
                        .keepAspectRatio(true)
                        .asBufferedImage();
                
                String sizeDir = optimizedDir + width + "w/";
                new File(sizeDir).mkdirs();
                String jpegPath = sizeDir + baseFilename + ".jpg";
                saveOptimizedImage(resized, jpegPath, "jpg", JPEG_QUALITY);
                
                // Generate WebP version only if supported
                if (WEBP_SUPPORTED) {
                    try {
                        String webpSizeDir = webpDir + width + "w/";
                        new File(webpSizeDir).mkdirs();
                        String webpSizePath = webpSizeDir + baseFilename + ".webp";
                        saveOptimizedImage(resized, webpSizePath, "webp", WEBP_QUALITY);
                    } catch (Exception e) {
                        // Skip WebP generation for this size if it fails
                    }
                }
            }
        }

        String imagePathPrefix = imageType.equals("categories") ? "/images/categories/" : "/images/projects/";
        
        Map<String, String> result = new HashMap<>();
        result.put("original", imagePathPrefix + "original/" + baseFilename + ".jpg");
        result.put("webp", imagePathPrefix + "optimized/webp/" + baseFilename + ".webp");
        result.put("baseFilename", baseFilename);
        
        return result;
    }

    private void saveOptimizedImage(BufferedImage image, String filePath, String format, float quality) throws IOException {
        File outputFile = new File(filePath);
        
        if (format.equalsIgnoreCase("webp") && WEBP_SUPPORTED) {
            try {
                // Try to write WebP using ImageIO
                boolean written = ImageIO.write(image, "webp", outputFile);
                if (!written) {
                    // Fallback to JPEG if WebP write fails
                    Thumbnails.of(image)
                            .size(image.getWidth(), image.getHeight())
                            .outputFormat("jpg")
                            .outputQuality(quality)
                            .toFile(outputFile);
                }
            } catch (Exception e) {
                // Fallback to JPEG if WebP fails
                Thumbnails.of(image)
                        .size(image.getWidth(), image.getHeight())
                        .outputFormat("jpg")
                        .outputQuality(quality)
                        .toFile(outputFile);
            }
        } else {
            // Save as JPEG with quality
            Thumbnails.of(image)
                    .size(image.getWidth(), image.getHeight())
                    .outputFormat("jpg")
                    .outputQuality(quality)
                    .toFile(outputFile);
        }
    }

    public String generateSrcSet(String baseUrl, String baseFilename, String format) {
        StringBuilder srcset = new StringBuilder();
        String formatExt = format.equals("webp") ? ".webp" : ".jpg";
        String formatPath = format.equals("webp") ? "optimized/webp/" : "optimized/";
        
        for (int width : RESPONSIVE_SIZES) {
            String url = baseUrl + "/images/projects/" + formatPath + width + "w/" + baseFilename + formatExt;
            if (srcset.length() > 0) {
                srcset.append(", ");
            }
            srcset.append(url).append(" ").append(width).append("w");
        }
        
        return srcset.toString();
    }

    public int getOptimalSize(int displayWidth) {
        for (int size : RESPONSIVE_SIZES) {
            if (size >= displayWidth) {
                return size;
            }
        }
        return RESPONSIVE_SIZES[RESPONSIVE_SIZES.length - 1]; // Return largest size
    }

    public boolean supportsWebP(String userAgent) {
        if (userAgent == null) return false;
        return userAgent.contains("Chrome") || 
               userAgent.contains("Opera") || 
               userAgent.contains("Edge") ||
               userAgent.contains("Firefox");
    }
}

