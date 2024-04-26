package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.ProductAttribute;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {
    @EntityGraph(attributePaths = {"attributeValue","attributeValue.attribute"})
    List<ProductAttribute> findAllByIdIn(List<Long> ids);
}
