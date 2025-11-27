package com.garden.api.reviews;

import com.garden.api.category.Category;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review mapToReview(ReviewRequest request, Category category) {
        Review review = new Review();
        review.setFullName(request.fullName());
        review.setLocation(request.location());
        review.setEmail(request.email());
        review.setStars(request.stars());
        review.setDescription(request.description());
        review.setCategory(category);
        review.setStatus(ReviewStatus.PENDING);
        return review;
    }


    public ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .fullName(review.getFullName())
                .location(review.getLocation())
                .email(review.getEmail())
                .stars(review.getStars())
                .description(review.getDescription())
                .categoryId(review.getCategory().getId())
                .categoryName(review.getCategory().getName())
                .status(review.getStatus())
                .build();
    }

    public void updateReviewEntity(Review review, ReviewRequest request, Category category) {
        review.setFullName(request.fullName());
        review.setLocation(request.location());
        review.setEmail(request.email());
        review.setStars(request.stars());
        review.setDescription(request.description());
        review.setCategory(category);
        review.setStatus(request.status());
    }

}
