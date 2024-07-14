package io.teamchallenge.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SignInRequestDto {
    @Email(message = "Please, insert valid email address")
    private String email;
    @Size(min = 8, max = 24, message = "Password length must be between 8 and 24 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–\\[{}\\]:;',?/*~$^+=<>])(?=\\S+$).*",
        message = "Password must contain at least one digit, one lowercase Latin character, "
            + "one uppercase Latin character and one special character")
    private String password;
}
