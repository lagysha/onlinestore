package io.teamchallenge.dto.attributes;

import jakarta.validation.constraints.NotBlank;
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
public class AttributeValuePatchRequestDto {
    @NotBlank
    @Size(min = 1,max = 100, message = "name is too long. Max size is 100 chars")
    private String value;
}
