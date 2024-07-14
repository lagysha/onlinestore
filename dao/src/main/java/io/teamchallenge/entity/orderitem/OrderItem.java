package io.teamchallenge.entity.orderitem;

import io.teamchallenge.entity.Order;
import io.teamchallenge.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order","product"})
@Builder
@EqualsAndHashCode(exclude = {"order","product"})
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price",nullable = false)
    private BigDecimal price;
}
