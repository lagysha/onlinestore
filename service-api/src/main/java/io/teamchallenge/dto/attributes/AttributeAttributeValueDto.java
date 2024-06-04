package io.teamchallenge.dto.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AttributeAttributeValueDto extends AttributeDto {
    @JsonProperty("values")
    private List<AttributeValueDto> attributeValueDtos;
}
