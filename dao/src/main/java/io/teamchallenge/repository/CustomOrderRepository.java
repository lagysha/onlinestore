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
    Page<Order> findAllByFilterParameters(OrderFilterDto filterParametersDto, Pageable pageable);
}
