package io.teamchallenge.dto.user;

import io.teamchallenge.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreviewDto {
    private Long id;

    private String firstName;

    private String lastName;

    private Role role;
}
