package io.teamchallenge.repository;

import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.cartitem.CartItem;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link Category} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds all attribute and attribute values for a given category.
     * Executes a JPQL query to retrieve distinct attribute and attribute values associated
     * with products in the specified category.
     * Joins the Product, ProductAttribute, AttributeValue, and Attribute entities
     * to retrieve attribute and attribute values.
     * Filters the results based on the provided category ID.
     *
     * @param categoryId The ID of the category.
     * @return A Stream of CategoryAttributeAttributeValueVO containing attribute
     *         and attribute values for the specified category.
     */
    @Query("select distinct "
        + "new io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO(a.id, a.name, av.id, av.value) "
        + "from Product p "
        + "join ProductAttribute pa on p.id = pa.product.id "
        + "join AttributeValue av on pa.attributeValue.id = av.id "
        + "join Attribute a on av.attribute.id = a.id "
        + "where p.category.id = :categoryId")
    Stream<CategoryAttributeAttributeValueVO> findAllAttributeAttributeValueByCategory(Long categoryId);

    Optional<Category> findByName(String name);
}