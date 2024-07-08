package io.teamchallenge.mapper;

import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.dto.user.ReviewerDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.util.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReviewToReviewResponseDtoMapperTest {
    @InjectMocks
    private ReviewToReviewResponseDtoMapper reviewToReviewResponseDtoMapper;

    @Test
    void convertTest() {
        Review source = Utils.getReview();
        ReviewResponseDto expected = ReviewResponseDto.builder()
            .text(source.getText())
            .rate(source.getRate())
            .createdAt(source.getCreatedAt())
            .user(ReviewerDto.builder()
                .firstName(source.getUser().getFirstName())
                .lastName(source.getUser().getLastName())
                .build())
            .build();
        ReviewResponseDto actual = reviewToReviewResponseDtoMapper.convert(source);
        assertEquals(expected,actual);
    }
}
