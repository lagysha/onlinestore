package io.teamchallenge.service;

import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.dto.user.ReviewerDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.ReviewRepository;
import io.teamchallenge.util.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void getAllByProductIdTest() {
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0,4);
        Review review = Utils.getReview();
        List<Review> reviews = List.of(review);
        Page<Review> page = new PageImpl<>(reviews, pageable, 1);
        ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
            .text(review.getText())
            .rate(review.getRate())
            .createdAt(review.getCreatedAt())
            .user(ReviewerDto.builder()
                .firstName(review.getUser().getFirstName())
                .lastName(review.getUser().getLastName())
                .build())
            .build();
        var expected = new PageableDto<>(
            List.of(reviewResponseDto),
            page.getTotalElements(),
            page.getPageable().getPageNumber(),
            page.getTotalPages());

        when(reviewRepository.findAllByProductId(productId, pageable)).thenReturn(page);
        when(modelMapper.map(review, ReviewResponseDto.class)).thenReturn(reviewResponseDto);

        var actual = reviewService.getAllByProductId(productId, pageable);

        verify(reviewRepository).findAllByProductId(productId, pageable);
        verify(modelMapper).map(review, ReviewResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    void createTest() {
        ReviewId reviewId = Utils.getReviewId();
        AddReviewRequestDto requestDto = Utils.getAddReviewRequestDto();
        Review review = Review.builder()
            .id(reviewId)
            .rate(requestDto.getRate())
            .text(requestDto.getText())
            .build();
        ReviewResponseDto expected = Utils.getReviewResponseDto();

        when(reviewRepository.save(review)).thenReturn(review);
        when(modelMapper.map(review, ReviewResponseDto.class)).thenReturn(expected);

        var actual = reviewService.create(reviewId, requestDto);

        verify(reviewRepository).save(review);
        verify(modelMapper).map(review, ReviewResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    void deleteByReviewIdTest() {
        ReviewId reviewId = Utils.getReviewId();

        when(reviewRepository.existsById(reviewId)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(reviewId);

        reviewService.deleteByReviewId(reviewId);

        verify(reviewRepository).existsById(reviewId);
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void deleteByReviewIdThrowsNotFoundExceptionTest() {
        ReviewId reviewId = Utils.getReviewId();

        when(reviewRepository.existsById(reviewId)).thenReturn(false);

        assertThrows(NotFoundException.class, ()->reviewService.deleteByReviewId(reviewId));
    }
}
