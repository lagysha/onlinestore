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
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

/**
 *  Implementation of a custom product repository interface that provides additional
 *  functionality beyond the standard CRUD operations.
 *  This implementation directly interacts with the underlying database through the EntityManager.
 *
 * @author Niktia Malov
 */
@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {
    private final EntityManager entityManager;

    /**
     * Finds the minimum and maximum prices of products based on the given specification.
     * Executes a CriteriaQuery to retrieve the minimum and maximum prices of products.
     * If a Specification is provided, filters the products based on the given criteria.
     *
     * @param specification The Specification to filter products (can be null).
     * @return A ProductMinMaxPriceDto object containing the minimum and maximum prices.
     */
    @Override
    public ProductMinMaxPriceDto findProductMinMaxPrice(@Nullable Specification<Product> specification) {
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

    /**
     * Finds all product IDs based on the given specification and pagination parameters.
     * Executes a CriteriaQuery to retrieve the IDs of products.
     * Applies pagination to limit the number of results.
     * If a Specification is provided, filters the products based on the given criteria.
     *
     * @param specification The Specification to filter products (can be null).
     * @param pageable The pagination parameters.
     * @return A Page object containing the IDs of products.
     */


    @Override
    public Page<Long> findAllProductIds(@Nullable Specification<Product> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);

        query.select(root.get("id"));
        if (Objects.nonNull(specification)) {
            query.where(specification.toPredicate(root, query, cb));
        }
        query.groupBy(root.get("id"));
        addSortPartToQuery(pageable, query, root, cb);

        List<Long> productIds = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize()).getResultList();

        var countQuery = cb.createQuery(Long.class);
        var rootCount = countQuery.from(Product.class);
        countQuery.select(cb.countDistinct(rootCount));

        if (Objects.nonNull(specification)) {
            countQuery.where(specification.toPredicate(rootCount, countQuery, cb));
        }

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(productIds, pageable, count);
    }

    private void addSortPartToQuery(Pageable pageable, CriteriaQuery<Long> query, Root<Product> root,
                                  CriteriaBuilder cb) {
        if(Objects.nonNull(pageable.getSort().getOrderFor("price"))){
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        } else if (Objects.nonNull(pageable.getSort().getOrderFor("popularity"))) {
            System.out.println("Popularity sorting");
        }else{
            System.out.println("Rating sorting");
        }
    }
}
