package io.teamchallenge.repository;

import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.cartitem.CartItem;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Repository interface for managing {@link Product} entities.
 * Provides methods to custom queries.
 * @author Niktia Malov
 */
public interface CustomProductRepository {
    /**
     * Finds the minimum and maximum prices of products based on the given specification.
     * If a Specification is provided, filters the products based on the given criteria.
     *
     * @param specification The Specification to filter products (can be null).
     * @return A ProductMinMaxPriceDto object containing the minimum and maximum prices.
     */
    ProductMinMaxPriceDto findProductMinMaxPrice(@Nullable Specification<Product> specification);

    /**
     * Finds all product IDs based on the given specification and pagination parameters.
     * Applies pagination to limit the number of results.
     * If a Specification is provided, filters the products based on the given criteria.
     *
     * @param specification The Specification to filter products (can be null).
     * @param pageable The pagination parameters.
     * @return A Page object containing the IDs of products.
     */
    Page<Long> findAllProductIds(@Nullable Specification<Product> specification, Pageable pageable);
}