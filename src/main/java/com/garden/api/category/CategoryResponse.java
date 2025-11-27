package com.garden.api.category;

import com.garden.api.projects.ProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CategoryResponse {

    Long id;

    String name;
    String description;
    String imageUrl;
    List<ProjectResponse> projects;

}
