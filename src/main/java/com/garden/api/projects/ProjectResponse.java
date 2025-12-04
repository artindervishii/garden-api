package com.garden.api.projects;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProjectResponse {

    Long id;
    String title;
    String description;
    List<String> categoriesName;
    List<String> images;
    List<String> videos;
    String clientName;
    BigDecimal price;
    ProjectStatus status;
    Integer displayOrder;
}
