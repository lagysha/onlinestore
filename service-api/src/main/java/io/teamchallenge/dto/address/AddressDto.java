package io.teamchallenge.dto.address;

import jakarta.validation.constraints.NotBlank;
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
public class AddressDto {
    @NotBlank
    private String addressLine;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String countryName;
}
