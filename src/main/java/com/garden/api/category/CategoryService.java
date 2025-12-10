package com.garden.api.category;

import com.garden.api.appointments.Appointment;
import com.garden.api.appointments.AppointmentRepository;
import com.garden.api.exceptions.DuplicateResourceException;
import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.projects.Project;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AppointmentRepository appointmentRepository;

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
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            file.transferTo(filePath.toFile());

            String url = baseUrl + "/images/categories/" + filename;

            Category category = categoryOpt.get();
            category.setImageUrl(url);
            categoryRepository.save(category);
            System.out.println("Saving file to: " + filePath.toAbsolutePath());

            return url;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}
