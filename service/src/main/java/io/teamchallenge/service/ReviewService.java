package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a paginated list of reviews for a specific product by its ID.
     *
     * @param productId the ID of the product whose reviews are to be retrieved.
     * @param pageable  the pagination information.
     * @return a {@link PageableDto} containing a list of {@link ReviewResponseDto} objects.
     */
    public PageableDto<ReviewResponseDto> getAllByProductId(Long productId, Pageable pageable) {
        Page<Review> page = reviewRepository.findAllByProductId(productId, pageable);

        return new PageableDto<>(
            page.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .toList(),
            page.getTotalElements(),
            page.getPageable().getPageNumber(),
            page.getTotalPages());
    }

    /**
     * Creates a new review for a product.
     *
     * @param reviewId            the composite ID of the review, which includes user ID and product ID.
     * @param addReviewRequestDto the data transfer object containing the details of the review to be added.
     * @return a {@link ReviewResponseDto} representing the newly created review.
     */
    @Transactional
    public ReviewResponseDto create(ReviewId reviewId, AddReviewRequestDto addReviewRequestDto) {
        //todo: add validation if user ordered this product

        Review review = Review.builder()
            .id(reviewId)
            .rate(addReviewRequestDto.getRate())
            .text(addReviewRequestDto.getText())
            .build();
        return modelMapper.map(reviewRepository.save(review), ReviewResponseDto.class);
    }

    /**
     * Deletes a review by its composite ID.
     *
     * @param reviewId the composite ID of the review to be deleted, which includes user ID and product ID.
     * @throws NotFoundException if the review does not exist.
     */
    @Transactional
    public void deleteByReviewId(ReviewId reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException(
                ExceptionMessage.REVIEW_NOT_FOUND_BY_ID.formatted(reviewId.getUserId(), reviewId.getProductId()));
        }
        reviewRepository.deleteById(reviewId);
    }
}
