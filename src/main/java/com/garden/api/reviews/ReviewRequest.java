package com.garden.api.reviews;

import jakarta.validation.constraints.*;

public record ReviewRequest(
        @NotBlank(message = "Full name cannot be empty")
        String fullName,

        @NotBlank(message = "Location cannot be empty")
        String location,

        String email,

        @Min(value = 1, message = "Stars must be at least 1")
        @Max(value = 5, message = "Stars cannot exceed 5")
        int stars,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        ReviewStatus status
) {}
