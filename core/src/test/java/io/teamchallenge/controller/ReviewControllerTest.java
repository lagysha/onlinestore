package io.teamchallenge.controller;

import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.service.ReviewService;
import io.teamchallenge.utils.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Test
    void getAllByProductIdTest() {
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0,6);
        List<ReviewResponseDto> content = List.of(Utils.getReviewResponseDto());
        PageableDto<ReviewResponseDto> expected = PageableDto.<ReviewResponseDto>builder()
            .page(content)
            .currentPage(0)
            .totalElements(1)
            .totalPages(1)
            .build();

        when(reviewService.getAllByProductId(productId, pageable)).thenReturn(expected);

        var actual = reviewController.getAllByProductId(productId, pageable);

        verify(reviewService).getAllByProductId(productId, pageable);
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void createTest() {
        ReviewId reviewId = Utils.getReviewId();
        ReviewResponseDto expected = Utils.getReviewResponseDto();
        AddReviewRequestDto addReviewRequestDto = Utils.getAddReviewRequestDto();

        when(reviewService.create(reviewId, addReviewRequestDto)).thenReturn(expected);

        var actual = reviewController.create(reviewId.getUserId(), reviewId.getProductId(), addReviewRequestDto);

        verify(reviewService).create(reviewId, addReviewRequestDto);
        assertEquals(HttpStatusCode.valueOf(201), actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void deleteTest() {
        ReviewId reviewId = Utils.getReviewId();

        var actual = reviewController.delete(reviewId.getUserId(), reviewId.getProductId());

        verify(reviewService).deleteByReviewId(reviewId);
        assertEquals(HttpStatusCode.valueOf(204), actual.getStatusCode());
        assertNull(actual.getBody());
    }

    @Test
    void deleteByAdminTest() {
        ReviewId reviewId = Utils.getReviewId();

        var actual = reviewController.deleteByAdmin(reviewId.getUserId(), reviewId.getProductId());

        verify(reviewService).deleteByReviewId(reviewId);
        assertEquals(HttpStatusCode.valueOf(204), actual.getStatusCode());
        assertNull(actual.getBody());
    }
}
