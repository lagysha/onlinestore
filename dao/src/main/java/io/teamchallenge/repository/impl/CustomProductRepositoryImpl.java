package io.teamchallenge.repository.impl;

import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.repository.CustomProductRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {
    private final EntityManager entityManager;

    @Override
    public ProductMinMaxPriceDto getProductMinMaxPrice(@Nullable Specification<Product> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductMinMaxPriceDto> query = cb.createQuery(ProductMinMaxPriceDto.class);

        Root<Product> root = query.from(Product.class);
        query.multiselect(
            cb.min(root.get("price")).alias("min"),
            cb.max(root.get("price")).alias("max"));

        if (Objects.nonNull(specification)) {
            query.where(specification.toPredicate(root, query, cb));
        }

        TypedQuery<ProductMinMaxPriceDto> typedQuery = entityManager.createQuery(query);

        return typedQuery.getSingleResult();
    }

    @Override
    public Page<Long> findAllProductIds(@Nullable Specification<Product> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<Product> root = query.from(Product.class);
        query.select(root.get("id"));

        if (Objects.nonNull(specification)) {
            query.where(specification.toPredicate(root, query, cb));
        }

        List<Long> productIds = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize()).getResultList();

        var countQuery = cb.createQuery(Long.class);
        var rootCount = countQuery.from(Product.class);
        countQuery.select(cb.count(rootCount));

        if (Objects.nonNull(specification)) {
            countQuery.where(specification.toPredicate(rootCount, countQuery, cb));
        }

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(productIds, pageable, count);
    }
}
