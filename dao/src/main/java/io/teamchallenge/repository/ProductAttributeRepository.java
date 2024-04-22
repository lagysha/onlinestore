package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {

    Boolean existsByProductIdAndAttributeValue_Attribute_Id(Long productId, Long attributeId);
}
