package com.garden.api.reviews;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    Long id;
    String fullName;
    String location;
    String email;
    int stars;
    String description;
    Long categoryId;
    String categoryName;
    ReviewStatus status;
}
