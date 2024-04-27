package io.teamchallenge.dto.security;

import io.teamchallenge.dto.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SignUpRequestDto {
    @Email(message = "Please, insert valid email address")
    private String email;
    @Size(min = 8, max = 24, message = "Password length must be between 8 and 24 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–\\[{}\\]:;',?/*~$^+=<>])(?=\\S+$).*",
        message = "Password must contain at least one digit, one lowercase Latin character, " +
            "one uppercase Latin character and one special character")
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phoneNumber;
    private AddressDto address;
}
