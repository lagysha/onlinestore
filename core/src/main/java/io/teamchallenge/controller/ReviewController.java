package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.annotation.CurrentUserId;
import io.teamchallenge.dto.PageableDto;
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

    @GetMapping("/{productId}")
    public ResponseEntity<PageableDto<ReviewResponseDto>> getAllByProductId(
        @PathVariable Long productId,
        @AllowedSortFields(values = {"rate", "createdAt"})
        @PageableDefault(sort = "rate", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAllByProductId(productId, pageable));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ReviewResponseDto> create(@CurrentUserId Long userId, @PathVariable Long productId,
                                                    @RequestBody AddReviewRequestDto addReviewRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reviewService.create(ReviewId.builder().userId(userId).productId(productId).build(),
                addReviewRequestDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@CurrentUserId Long userId, @PathVariable Long productId) {
        reviewService.deleteByReviewId(ReviewId.builder().userId(userId).productId(productId).build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{productId}/{userId}")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable Long userId, @PathVariable Long productId) {
        reviewService.deleteByReviewId(ReviewId.builder().userId(userId).productId(productId).build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
