package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.annotation.CurrentUserId;
import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Retrieves a paginated list of reviews for a specific product by its ID, allowing sorting by specified fields.
     *
     * @param productId the ID of the product whose reviews are to be retrieved.
     * @param pageable  the pagination and sorting information.
     * @return a {@link ResponseEntity} containing a {@link PageableDto} with the reviews.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<PageableDto<ReviewResponseDto>> getAllByProductId(
        @PathVariable Long productId,
        @AllowedSortFields(values = {"rate", "createdAt"})
        @PageableDefault(sort = "rate", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAllByProductId(productId, pageable));
    }

    /**
     * Creates a new review for a specific product.
     *
     * @param userId the ID of the user creating the review.
     * @param productId the ID of the product being reviewed.
     * @param addReviewRequestDto the data transfer object containing the details of the review to be added.
     * @return a {@link ResponseEntity} containing the created {@link ReviewResponseDto}.
     */
    @PostMapping("/{productId}")
    public ResponseEntity<ReviewResponseDto> create(@CurrentUserId Long userId, @PathVariable Long productId,
                                                    @RequestBody AddReviewRequestDto addReviewRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reviewService.create(ReviewId.builder().userId(userId).productId(productId).build(),
                addReviewRequestDto));
    }

    /**
     * Deletes a review for a specific product created by the current user.
     *
     * @param userId the ID of the current user.
     * @param productId the ID of the product whose review is to be deleted.
     * @return a {@link ResponseEntity} with HTTP status NO_CONTENT.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@CurrentUserId Long userId, @PathVariable Long productId) {
        reviewService.deleteByReviewId(ReviewId.builder().userId(userId).productId(productId).build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Deletes a review for a specific product by a specific user. This endpoint is intended for admin use.
     *
     * @param userId the ID of the user who created the review.
     * @param productId the ID of the product whose review is to be deleted.
     * @return a {@link ResponseEntity} with HTTP status NO_CONTENT.
     */
    @DeleteMapping("/{productId}/{userId}")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable Long userId, @PathVariable Long productId) {
        reviewService.deleteByReviewId(ReviewId.builder().userId(userId).productId(productId).build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
