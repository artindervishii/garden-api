package com.garden.api.reviews;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Long> createReview(@Valid @RequestBody ReviewRequest request) {
        Long id = reviewService.createReview(request);
        return ResponseEntity.ok(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateReview(@PathVariable Long id,@Valid @RequestBody ReviewRequest request) {
        reviewService.updateReview(id,request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @GetMapping
    public List<ReviewResponse> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/category/{categoryId}")
    public List<ReviewResponse> getByCategory(@PathVariable Long categoryId) {
        return reviewService.getReviewsByCategory(categoryId);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/status/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateReviewStatus(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewStatusUpdateRequest request
    ) {
        reviewService.updateReviewStatus(reviewId, request.status());
        return ResponseEntity.noContent().build();
    }


}
