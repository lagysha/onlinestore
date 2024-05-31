package io.teamchallenge.dto.category;

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
public class CategoryAttributeAttributeValueVO {
    private Long attributeId;
    private String attributeName;
    private Long attributeValueId;
    private String attributeValueName;
}
