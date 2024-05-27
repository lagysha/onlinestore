package io.teamchallenge.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductAttributeResponseDto {
    private String name;
    private String value;
}