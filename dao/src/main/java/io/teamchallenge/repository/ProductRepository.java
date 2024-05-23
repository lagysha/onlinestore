package io.teamchallenge.repository;

import io.teamchallenge.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Retrieves a Product by its ID along with associated collections if available.
     * If the Product is found, associated collections such as images are eagerly fetched.
     *
     * @param id The ID of the Product to retrieve.
     * @return An Optional containing the Product if found, with associated collections eagerly fetched.
     */
    default Optional<Product> findByIdWithCollections(Long id) {
        var product = findByIdWithCategoryAndBrandAndProductAttribute(id);
        if (product.isPresent()) {
            product = findByIdWithImages(id);
        }
        return product;
    }

    /**
     * Retrieves a page of Product IDs by name, optionally filtering by name.
     *
     * @param pageable The Pageable object containing pagination and sorting information.
     * @param name     The name to filter the products by (can be null).
     * @return A Page containing the IDs of products matching the provided name.
     */
    @Query("select p.id from Product p "
        + "where (:name is NULL or p.name like %:name%)")
    Page<Long> findAllIdsByName(Pageable pageable, String name);

    /**
     * Retrieves all Products by their IDs with associated images eagerly fetched.
     *
     * @param productIds The list of IDs of the Products to retrieve.
     * @return A list of Products with associated images eagerly fetched.
     */
    @Query("select p from Product p join fetch p.images where p.id in :productIds")
    List<Product> findAllByIdWithImages(@Param("productIds") List<Long> productIds);

    /**
     * Retrieves a Product by its ID with associated category, brand, and product attributes eagerly fetched.
     *
     * @param id The ID of the Product to retrieve.
     * @return An Optional containing the Product if found, with associated category, brand,
     *         and product attributes eagerly fetched.
     */
    @Query("select p from Product p "
        + "join fetch p.category "
        + "join fetch p.brand "
        + "join fetch p.productAttributes pa "
        + "join fetch pa.attributeValue "
        + "join fetch pa.attributeValue.attribute "
        + "where p.id in :productId ")
    Optional<Product> findByIdWithCategoryAndBrandAndProductAttribute(@Param("productId") Long id);

    /**
     * Retrieves a Product by its ID with associated images eagerly fetched.
     *
     * @param id The ID of the Product to retrieve.
     * @return An Optional containing the Product if found, with associated images eagerly fetched.
     */
    @Query("select p from Product p join fetch p.images where p.id in :productId ")
    Optional<Product> findByIdWithImages(@Param("productId") Long id);

    /**
     * Retrieves a Product by its name.
     *
     * @param name The name of the Product to retrieve.
     * @return An Optional containing the Product if found by the provided name.
     */
    Optional<Product> findByName(String name);

    /**
     * Retrieves a Product by its name and ID not matching the provided ID.
     *
     * @param name The name of the Product to retrieve.
     * @param id   The ID of the Product to exclude from the search.
     * @return An Optional containing the Product if found by the provided name and ID not matching the provided ID.
     */
    Optional<Product> findByNameAndIdNot(String name, Long id);
}
