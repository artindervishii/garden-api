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

    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

}
