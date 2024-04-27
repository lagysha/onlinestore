package io.teamchallenge.dto.security;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
