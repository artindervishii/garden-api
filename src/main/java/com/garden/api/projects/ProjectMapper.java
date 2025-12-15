package com.garden.api.projects;

import com.garden.api.category.Category;
import com.garden.api.category.CategoryRepository;
import com.garden.api.clients.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ProjectMapper {

    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;

    public Project map(ProjectRequest request) {
        Project project = new Project();
        map(project, request);
        return project;
    }

    public void map(Project project, ProjectRequest request) {
        if(request.getClientId() != null) {
            clientRepository.findById(request.getClientId()).ifPresent(project::setClient);
        }
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setPrice(request.getPrice());
        project.setStatus(request.getStatus());
        if (request.getBeforeImage() != null) {
            project.setBeforeImage(request.getBeforeImage());
        }
        if (request.getAfterImage() != null) {
            project.setAfterImage(request.getAfterImage());
        }
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
                .images(project.getImages())
                .videos(project.getVideos())
                .beforeImage(project.getBeforeImage())
                .afterImage(project.getAfterImage())
                .description(project.getDescription())
                .categoriesName(mapToCategories(project.getCategories()))
                .clientName(project.getClient() != null ? project.getClient().getName() : null)
                .price(project.getPrice())
                .status(project.getStatus())
                .displayOrder(project.getDisplayOrder())
                .build();
    }

    private List<String> mapToCategories(List<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }

}
