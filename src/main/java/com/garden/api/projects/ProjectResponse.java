package com.garden.api.projects;

import lombok.Builder;

import java.util.List;

@Builder
public class ProjectResponse {

    Long id;
    String title;
    String image;
    String description;
    List<String> categoriesName;
}
