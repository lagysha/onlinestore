package io.teamchallenge.service;

import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.dto.user.ReviewerDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.User;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.ForbiddenException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.ReviewRepository;
import io.teamchallenge.repository.UserRepository;
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
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

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
        User user = Utils.getUser();
        Product product = Utils.getProduct();
        Review review = Review.builder()
            .id(reviewId)
            .rate(requestDto.getRate())
            .text(requestDto.getText())
            .user(user)
            .product(product)
            .build();
        ReviewResponseDto expected = Utils.getReviewResponseDto();


        when(reviewRepository.save(review)).thenReturn(review);
        when(modelMapper.map(review, ReviewResponseDto.class)).thenReturn(expected);
        when(userRepository.existsByIdAndCompletedOrderWithProductId(reviewId.getUserId(), reviewId.getProductId()))
            .thenReturn(true);
        when(userRepository.getReferenceById(reviewId.getUserId())).thenReturn(user);
        when(productRepository.getReferenceById(reviewId.getProductId())).thenReturn(product);
        when(reviewRepository.existsById(reviewId)).thenReturn(false);

        var actual = reviewService.create(reviewId, requestDto);

        assertEquals(expected, actual);
        verify(reviewRepository).save(review);
        verify(modelMapper).map(review, ReviewResponseDto.class);
        verify(userRepository).getReferenceById(reviewId.getUserId());
        verify(productRepository).getReferenceById(reviewId.getProductId());
        verify(reviewRepository).existsById(reviewId);
    }

    @Test
    void createThrowsForbiddenExceptionWhenUserHasNoCompletedOrdersWithProductTest() {
        ReviewId reviewId = Utils.getReviewId();
        AddReviewRequestDto requestDto = Utils.getAddReviewRequestDto();

        when(userRepository.existsByIdAndCompletedOrderWithProductId(reviewId.getUserId(), reviewId.getProductId()))
            .thenReturn(false);

        assertThrows(ForbiddenException.class, ()->reviewService.create(reviewId, requestDto));
        verify(userRepository).existsByIdAndCompletedOrderWithProductId(reviewId.getUserId(), reviewId.getProductId());
    }

    @Test
    void createThrowsAlreadyExistsExceptionWhenUserHasReviewOnProductTest() {
        ReviewId reviewId = Utils.getReviewId();
        AddReviewRequestDto requestDto = Utils.getAddReviewRequestDto();

        when(userRepository.existsByIdAndCompletedOrderWithProductId(reviewId.getUserId(), reviewId.getProductId()))
            .thenReturn(true);
        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, ()->reviewService.create(reviewId, requestDto));
        verify(userRepository).existsByIdAndCompletedOrderWithProductId(reviewId.getUserId(), reviewId.getProductId());
        verify(reviewRepository).existsById(reviewId);
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
