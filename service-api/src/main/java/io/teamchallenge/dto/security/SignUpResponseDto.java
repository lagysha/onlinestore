package io.teamchallenge.dto.security;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SignUpResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
