package io.teamchallenge.repository;

import io.teamchallenge.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p.id from Product p " +
        "where (:name is NULL or p.name like %:name%)")
    Page<Long> findAllProductsIdByName(Pageable pageable, String name);

    @Query("select p from Product p " +
        "join fetch p.category " +
        "join fetch p.brand " +
        "join fetch p.productAttributes pa " +
        "join fetch pa.attributeValue "+
        "join fetch pa.attributeValue.attribute "+
        "where p.id in :productIds")
    List<Product> findAllByIdWithCategoryAndBrandAndProductAttribute(@Param("productIds") List<Long> productIds);
}
