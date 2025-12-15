package com.garden.api.category;

import com.garden.api.appointments.Appointment;
import com.garden.api.appointments.AppointmentRepository;
import com.garden.api.common.ImageOptimizationService;
import com.garden.api.exceptions.DuplicateResourceException;
import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.projects.Project;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AppointmentRepository appointmentRepository;
    private final ImageOptimizationService imageOptimizationService;

    @Transactional
    public Long createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new DuplicateResourceException("Category with name " + categoryRequest.getName() + " already exists.");
        }
        Category category = categoryRepository.save(categoryMapper.mapToCategory(categoryRequest));
        return category.getId();
    }

    public Page<CategoryResponse> findAllCategories(Pageable pageable) {
        return categoryRepository.findAllCategories(pageable).map(categoryMapper::mapToCategoryResponse);
    }

    public CategoryResponse findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).map(categoryMapper::mapToCategoryResponse).orElseThrow(() -> new ResourceNotFoundException("Category with ID: " + categoryId + " not found"));
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID: " + categoryId + " not found"));

        for (Project project : category.getProjects()) {
            project.getCategories().remove(category);
        }

        for (Appointment appointment : appointmentRepository.findByCategory(category)) {
            appointment.setCategory(null);
        }

        categoryRepository.delete(category);
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category with ID: " + categoryId + " not found");
        }
        Optional<Category> categoryResponse = categoryRepository.findById(categoryId);
        if (categoryResponse.isPresent()) {
            Category category = categoryResponse.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            categoryRepository.save(category);
        }
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public String uploadCategoryImage(Long categoryId, MultipartFile file) {

        String baseUrl = "https://api.garten-er.de";

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/categories/";

            String originalFilename = file.getOriginalFilename();
            String baseFilename = System.currentTimeMillis() + "-" +
                    (originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_") : "category");

            Map<String, String> optimizedUrls = imageOptimizationService.optimizeImage(file, uploadDir, baseFilename, "categories");
            String url = baseUrl + optimizedUrls.get("original");

            Category category = categoryOpt.get();
            category.setImageUrl(url);
            categoryRepository.save(category);
            System.out.println("Saved optimized category image: " + url);

            return url;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload and optimize image", e);
        }
    }

    @Transactional
    public void deleteCategoryImage(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + categoryId + " not found"));

        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            try {
                String imageUrl = category.getImageUrl();
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                String uploadDir = System.getProperty("user.dir") + "/uploads/categories/";

                // Delete original image (in original/ folder)
                Path originalFilePath = Paths.get(uploadDir + "original/" + filename);
                if (Files.exists(originalFilePath)) {
                    Files.delete(originalFilePath);
                    System.out.println("Deleted category original image file: " + originalFilePath.toAbsolutePath());
                }

                // Also check if image exists directly in categories folder (for older uploads)
                Path directFilePath = Paths.get(uploadDir + filename);
                if (Files.exists(directFilePath)) {
                    Files.delete(directFilePath);
                    System.out.println("Deleted category image file: " + directFilePath.toAbsolutePath());
                }

                // Delete optimized images - all sizes
                String[] sizes = {"320w", "640w", "768w", "1024w", "1280w", "1920w", "2560w"};
                for (String size : sizes) {
                    Path optimizedFilePath = Paths.get(uploadDir + "optimized/" + size + "/" + filename);
                    if (Files.exists(optimizedFilePath)) {
                        Files.delete(optimizedFilePath);
                        System.out.println("Deleted category optimized image file: " + optimizedFilePath.toAbsolutePath());
                    }
                }

                // Delete webp versions
                String webpFilename = filename.replaceAll("\\.(jpg|jpeg|png)$", ".webp");
                Path webpFilePath = Paths.get(uploadDir + "optimized/webp/" + webpFilename);
                if (Files.exists(webpFilePath)) {
                    Files.delete(webpFilePath);
                    System.out.println("Deleted category webp image file: " + webpFilePath.toAbsolutePath());
                }

                // Delete webp versions in size folders
                for (String size : sizes) {
                    Path webpSizeFilePath = Paths.get(uploadDir + "optimized/webp/" + size + "/" + webpFilename);
                    if (Files.exists(webpSizeFilePath)) {
                        Files.delete(webpSizeFilePath);
                        System.out.println("Deleted category webp image file: " + webpSizeFilePath.toAbsolutePath());
                    }
                }

            } catch (IOException e) {
                System.err.println("Failed to delete category image file: " + e.getMessage());
            }
        }

        category.setImageUrl(null);
        categoryRepository.save(category);
    }

}
