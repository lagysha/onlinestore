package io.teamchallenge.service;

import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
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

    @Transactional
    public void deleteByReviewId(ReviewId reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
