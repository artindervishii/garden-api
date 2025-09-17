package com.garden.api.projects;

import com.garden.api.category.Category;
import com.garden.api.category.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ProjectMapper {

    private final CategoryRepository categoryRepository;

    public Project map(ProjectRequest request) {
        Project project = new Project();
        map(project, request);
        return project;
    }

    public void map(Project project, ProjectRequest request) {
        project.setTitle(request.getTitle());
        project.setImage(request.getImage());
        project.setDescription(request.getDescription());
        project.setStatus(ProjectStatus.Scheduled);
        project.setPrice(request.getPrice());

        List<Category> categories = mapCategoriesByIds(request.getCategoryIds());
        project.setCategories(categories);
    }

    private List<Category> mapCategoriesByIds(List<Long> categoryIds) {
        if (categoryIds == null) {
            return List.of();
        }
        return categoryIds.stream().map(this::findCategoryById).collect(Collectors.toList());
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }


    public ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .image(project.getImage())
                .description(project.getDescription())
                .categoriesName(mapToCategories(project.getCategories()))
                .build();
    }

    private List<String> mapToCategories(List<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }

}
