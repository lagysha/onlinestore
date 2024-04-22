package io.teamchallenge.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductAttributeRequestDto {
    private Long attributeValueId;
}
