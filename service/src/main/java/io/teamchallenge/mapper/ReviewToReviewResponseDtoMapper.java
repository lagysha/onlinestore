package io.teamchallenge.mapper;

import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.dto.user.ReviewerDto;
import io.teamchallenge.entity.reviews.Review;
import org.modelmapper.AbstractConverter;

public class ReviewToReviewResponseDtoMapper extends AbstractConverter<Review, ReviewResponseDto> {
    @Override
    protected ReviewResponseDto convert(Review source) {
        return ReviewResponseDto.builder()
            .text(source.getText())
            .rate(source.getRate())
            .createdAt(source.getCreatedAt())
            .user(ReviewerDto.builder()
                .firstName(source.getUser().getFirstName())
                .lastName(source.getUser().getLastName())
                .build())
            .build();
    }
}
