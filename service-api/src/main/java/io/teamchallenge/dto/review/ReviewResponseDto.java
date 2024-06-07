package io.teamchallenge.dto.review;

import io.teamchallenge.dto.user.UserPreviewDto;
import io.teamchallenge.enumerated.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private UserPreviewDto user;

    private String text;

    private Short rate;

    private LocalDateTime createdAt;
}
