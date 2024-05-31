package io.teamchallenge.repository;

import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.entity.Category;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
        select distinct new io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO(a.id, a.name,av.id, av.value)
        from Product p
                 join ProductAttribute pa on p.id = pa.product.id
                 join AttributeValue av on pa.attributeValue.id = av.id
                 join Attribute a on av.attribute.id = a.id
        where p.category.id = :categoryId
        """)
    Stream<CategoryAttributeAttributeValueVO> getAttributeAttributeValueByCategory(Long categoryId);
}
