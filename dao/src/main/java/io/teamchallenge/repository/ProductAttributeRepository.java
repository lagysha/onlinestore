package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.ProductAttribute;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Niktia Malov
 */
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    /**
     * Retrieves a list of ProductAttributes by their IDs, eagerly fetching the associated attribute values
     * and their attributes.
     *
     * @param ids The list of IDs of the ProductAttributes to retrieve.
     * @return A list of ProductAttributes with the associated attribute values and their attributes eagerly fetched.
     */
    @EntityGraph(attributePaths = {"attributeValue", "attributeValue.attribute"})
    List<ProductAttribute> findAllByIdIn(List<Long> ids);
}
