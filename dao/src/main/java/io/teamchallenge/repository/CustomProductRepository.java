package io.teamchallenge.repository;

import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.entity.Product;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CustomProductRepository {
    ProductMinMaxPriceDto getProductMinMaxPrice(Specification<Product> specification);

    Page<Long> findAllProductIds(@Nullable Specification<Product> specification, Pageable pageable);
}
