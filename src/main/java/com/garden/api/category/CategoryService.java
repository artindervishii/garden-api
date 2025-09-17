package com.garden.api.category;

import com.garden.api.exceptions.DuplicateResourceException;
import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.projects.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new DuplicateResourceException("Category with name " + categoryRequest.getName() + " already exists.");
        }
        categoryRepository.save(categoryMapper.mapToCategory(categoryRequest));
    }

    public Page<CategoryResponse> findAllCategories(Pageable pageable) {
        return categoryRepository.findAllCategories(pageable).map(categoryMapper::mapToCategoryResponse);
    }

    public CategoryResponse findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).map(categoryMapper::mapToCategoryResponse).orElseThrow(() -> new ResourceNotFoundException("Category with ID: " + categoryId + " not found"));
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category with ID: " + categoryId + " not found");
        }
        projectRepository.deleteCategoryFromProjects(categoryId);
        categoryRepository.deleteById(categoryId);
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
            categoryRepository.save(category);
        }
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

}
