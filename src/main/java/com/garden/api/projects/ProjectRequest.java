package com.garden.api.projects;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProjectRequest {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "At least one category ID is required")
    private List<Long> categoryIds;

    private List<String> images;

    private List<String> videos;

    private String beforeImage;

    private String afterImage;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long clientId;

    private ProjectStatus status;

    private BigDecimal price;
}
