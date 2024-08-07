package io.teamchallenge.repository;

import io.teamchallenge.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
    /**
     * Finds an order by its ID, fetching all related data such as address, post address, order items,
     * and products associated with the order items.
     *
     * @param orderId the ID of the order to retrieve.
     * @return an {@link Optional} containing the order if found, otherwise empty.
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.address a "
           + "LEFT JOIN FETCH o.postAddress LEFT JOIN FETCH o.orderItems oi "
           + "LEFT JOIN FETCH a.country LEFT JOIN FETCH oi.product "
           + "WHERE o.id = :orderId")
    Optional<Order> findByIdFetchData(Long orderId);
}
