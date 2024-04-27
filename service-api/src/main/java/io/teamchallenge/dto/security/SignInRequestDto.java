package io.teamchallenge.dto.security;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SignInRequestDto {
    private String email;
    private String password;
}
