package com.garden.api.category;

import com.garden.api.projects.ProjectResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category mapToCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        return category;
    }

    public CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .projects(category.getProjects() != null ?
                        category.getProjects().stream().map(p -> ProjectResponse.builder()
                                .id(p.getId())
                                .title(p.getTitle())
                                .images(p.getImages())
                                .description(p.getDescription())
                                .build()).collect(Collectors.toList())
                        : null)
                .build();
    }
}
