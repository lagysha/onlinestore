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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AttributeResponseDto {
    private Long id;
    @NotBlank
    @Size(min = 1,max = 100, message = "name is too long. Max size is 100 chars")
    private String name;
}
