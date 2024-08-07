package io.teamchallenge.repository;

import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for getting {@link Order} entities.
 * Provides methods to custom queries.
 * @author Denys Liubchenko
 */
public interface CustomOrderRepository {
    /**
     * Finds all orders that match the given filter parameters and pagination information.
     *
     * @param filterParametersDto the filter parameters used to narrow down the search for orders.
     *                            This could include various fields such as order status, date range,
     *                            customer details, etc.
     * @param pageable            the pagination information specifying the page number, page size, and sorting options.
     * @return a page of orders that match the given filter parameters and pagination settings.
     */
    Page<Order> findAllByFilterParameters(OrderFilterDto filterParametersDto, Pageable pageable);
}
