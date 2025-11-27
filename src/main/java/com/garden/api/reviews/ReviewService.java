package com.garden.api.reviews;

import com.garden.api.category.Category;
import com.garden.api.category.CategoryRepository;
import com.garden.api.email.EmailService;
import com.garden.api.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewMapper reviewMapper;
    private final EmailService emailService;

    @Transactional
    public Long createReview(ReviewRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Review review = reviewMapper.mapToReview(request, category);
        reviewRepository.save(review);

        emailService.sendReviewEmail(review);

        return review.getId();
    }

    @Transactional
    public void updateReview(Long id, ReviewRequest request) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        reviewMapper.updateReviewEntity(review, request, category);

        reviewRepository.save(review);
    }



    public List<ReviewResponse> getReviewsByCategory(Long categoryId) {
        return reviewRepository.findByCategoryId(categoryId)
                .stream()
                .map(reviewMapper::mapToReviewResponse)
                .toList();
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::mapToReviewResponse)
                .toList();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review with ID " + reviewId + " not found");
        }

        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void updateReviewStatus(Long reviewId, ReviewStatus status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setStatus(status);
        reviewRepository.save(review);
    }

}
