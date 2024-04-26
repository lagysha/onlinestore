package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.attributes.ProductAttribute;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValue,Long> {

    @EntityGraph(attributePaths = {"attribute"})
    List<AttributeValue> findAllByIdIn(List<Long> ids);
}
