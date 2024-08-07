package io.teamchallenge.repository.impl;

import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.entity.Address;
import io.teamchallenge.entity.Order;
import io.teamchallenge.entity.orderitem.OrderItem;
import io.teamchallenge.repository.CustomOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    public static final String ORDER_ITEMS = "orderItems";
    public static final String POST_ADDRESS = "postAddress";
    public static final String ADDRESS = "address";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    private final EntityManager em;
    private final CriteriaBuilder cb;

    @Autowired
    public CustomOrderRepositoryImpl(EntityManager em) {
        this.em = em;
        cb = em.getCriteriaBuilder();
    }

    /**
     * Retrieves a paginated list of order entities based on the specified filter
     * parameters.
     *
     * @param filterParametersDto
     *            the filter parameters to apply when retrieving the order entities
     * @param pageable
     *            the pagination information
     * @return a paginated list of order entities that match the filter parameters
     */
    @Override
    public Page<Order> findAllByFilterParameters(OrderFilterDto filterParametersDto, Pageable pageable) {
        var mainQuery = cb.createQuery(Order.class);
        var mainRoot = mainQuery.from(Order.class);

        Join<Order, OrderItem> oij = mainRoot.join(ORDER_ITEMS, JoinType.LEFT);

        var order = getOrder(pageable, mainRoot, oij);
        List<Predicate> predicates = getAllPredicates(mainRoot, filterParametersDto);
        List<Predicate> totalPredicates = getTotalPredicates(oij, filterParametersDto);

        mainQuery.where(predicates.toArray(new Predicate[0]))
            .groupBy(mainRoot.get("id"))
            .orderBy(order);

        if (!totalPredicates.isEmpty()) {
            mainQuery.having(totalPredicates.toArray(new Predicate[0]));
        }

        TypedQuery<Order> countTotalTypedQuery = em.createQuery(mainQuery);
        List<Order> firstResults = countTotalTypedQuery.setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize()).getResultList();

        if (firstResults.isEmpty()) {
            return new PageImpl<>(firstResults, pageable, 0L);
        }

        List<Long> ids = firstResults.stream().map(Order::getId).toList();

        fetchDataByIds(ids);

        Long totalCount = getTotalCount(filterParametersDto);

        return new PageImpl<>(firstResults, pageable, totalCount);
    }

    private void fetchDataByIds(List<Long> ids) {
        var query = cb.createQuery(Order.class);
        var root = query.from(Order.class);

        root.fetch(ORDER_ITEMS, JoinType.LEFT);
        root.fetch(POST_ADDRESS, JoinType.LEFT);
        Fetch<Order, Address> af = root.fetch(ADDRESS, JoinType.LEFT);
        af.fetch("country", JoinType.LEFT);

        query.where(root.get("id").in(ids.toArray()));

        em.createQuery(query).getResultList();
    }

    private List<jakarta.persistence.criteria.Order> getOrder(Pageable pageable, Root<Order> countTotalRoot,
                                 Join<Order, OrderItem> oij) {
        Sort pageableSort = pageable.getSort();
        List<jakarta.persistence.criteria.Order> orders = new LinkedList<>();

        pageableSort.forEach(order -> {
            if (order.getProperty().equals("total")) {
                var totalPriceExpression = cb.sum(cb.prod(oij.get(PRICE), oij.get(QUANTITY)));
                jakarta.persistence.criteria.Order totalOrder = order.getDirection().isAscending()
                    ? cb.asc(totalPriceExpression)
                    : cb.desc(totalPriceExpression);
                orders.add(totalOrder);
            } else {
                orders.addAll(QueryUtils.toOrders(Sort.by(order), countTotalRoot, cb));
            }
        });

        return orders;
    }

    private Long getTotalCount(OrderFilterDto filterParametersDto) {
        var countQuery = cb.createQuery(UUID.class);
        var countRoot = countQuery.from(Order.class);

        Join<Order, OrderItem> oij = countRoot.join(ORDER_ITEMS, JoinType.LEFT);

        List<Predicate> predicatesCount = getAllPredicates(countRoot, filterParametersDto);
        List<Predicate> totalCountPredicates = getTotalPredicates(oij, filterParametersDto);
        countQuery.select(countRoot.get("id")).where(predicatesCount.toArray(new Predicate[0]))
            .groupBy(countRoot.get("id"));

        if (!totalCountPredicates.isEmpty()) {
            countQuery.having(totalCountPredicates.toArray(new Predicate[0]));
        }

        CriteriaQuery<Long> query = ((JpaCriteriaQuery<UUID>) countQuery).createCountQuery();
        return em.createQuery(query).getSingleResult();
    }

    private List<Predicate> getTotalPredicates(Join<Order, OrderItem> oij,
                                               OrderFilterDto filterParametersDto) {
        List<Predicate> predicates = new LinkedList<>();
        if (filterParametersDto.getTotalLess() != null) {
            predicates.add(cb.le(cb.sum(cb.prod(oij.get(PRICE), oij.get(QUANTITY))), filterParametersDto.getTotalLess()));
        }
        if (filterParametersDto.getTotalMore() != null) {
            predicates.add(cb.ge(cb.sum(cb.prod(oij.get(PRICE), oij.get(QUANTITY))), filterParametersDto.getTotalMore()));
        }
        return predicates;
    }

    private List<Predicate> getAllPredicates(Root<Order> root, OrderFilterDto filterParametersDto) {
        List<Predicate> predicates = new LinkedList<>();
        if (filterParametersDto.getIsPaid() != null) {
            predicates.add(cb.equal(root.get("isPaid"), filterParametersDto.getIsPaid()));
        }
        if (filterParametersDto.getDeliveryMethods() != null && !filterParametersDto.getDeliveryMethods().isEmpty()) {
            predicates.add(root.get("deliveryMethod").in(filterParametersDto.getDeliveryMethods().toArray()));
        }
        if (filterParametersDto.getStatuses() != null && !filterParametersDto.getStatuses().isEmpty()) {
            predicates.add(root.get("deliveryStatus").in(filterParametersDto.getStatuses().toArray()));
        }
        if (filterParametersDto.getCreatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filterParametersDto.getCreatedBefore()));
        }
        if (filterParametersDto.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filterParametersDto.getCreatedAfter()));
        }
        return predicates;
    }
}
