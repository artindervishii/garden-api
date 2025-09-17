package com.garden.api.projects;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectResponse {

    Long id;
    String title;
    String image;
    String description;
    List<String> categoriesName;
}
