package com.garden.api.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class CategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    String name;
    String description;
    String imageUrl;
}
