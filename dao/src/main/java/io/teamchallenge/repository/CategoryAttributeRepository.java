package io.teamchallenge.repository;

import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.entity.attributes.CategoryAttribute;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for managing {@link CategoryAttribute} entities.
 * @author Niktia Malov
 */
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute,Long> {

    /**
     * Finds all attributes and their corresponding attribute values by category ID.
     * <p>
     * This method executes a custom query to retrieve distinct combinations of attributes
     * and their values for a given category.
     * </p>
     *
     * @param categoryId the ID of the category for which to find attributes and attribute values
     * @return a {@link Stream} of {@link CategoryAttributeAttributeValueVO} containing the attribute
     * and attribute value details
     */
    @Query("select distinct "
        + "new io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO(a.id, a.name, av.id, av.value) "
        + "from CategoryAttribute ca "
        + "join Attribute a on a.id = ca.attribute.id "
        + "join AttributeValue av on av.attribute.id = a.id "
        + "where ca.category.id = :categoryId")
    Stream<CategoryAttributeAttributeValueVO> findAllAttributeAttributeValueByCategory(Long categoryId);
}
