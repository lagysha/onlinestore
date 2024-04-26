package io.teamchallenge.repository;

import io.teamchallenge.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {

    default Optional<Product> findByIdWithCollections(Long id) {
        var product = findByIdWithCategoryAndBrandAndProductAttribute(id);
        if (product.isPresent()) {
            product = findByIdWithImages(id);
        }
        return product;
    }

    @Query("select p.id from Product p " +
        "where (:name is NULL or p.name like %:name%)")
    Page<Long> findAllProductsIdByName(Pageable pageable, String name);

    @Query("select p from Product p join fetch p.images")
    List<Product> findAllByIdWithImages(@Param("productIds") List<Long> productIds);

    @Query("select p from Product p " +
        "join fetch p.category " +
        "join fetch p.brand " +
        "join fetch p.productAttributes pa " +
        "join fetch pa.attributeValue "+
        "join fetch pa.attributeValue.attribute " +
        "where p.id in :productId ")
    Optional<Product> findByIdWithCategoryAndBrandAndProductAttribute(@Param("productId") Long id);

    @Query("select p from Product p " +
        "join fetch p.images " +
        "where p.id in :productId ")
    Optional<Product> findByIdWithImages(@Param("productId") Long id);

    Optional<Product> findByName(String name);

    Optional<Product> findByNameAndIdNot(String name,Long id);
}
