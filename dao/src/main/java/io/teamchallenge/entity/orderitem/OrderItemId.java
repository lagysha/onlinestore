package io.teamchallenge.entity.orderitem;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
@EqualsAndHashCode
public class OrderItemId implements Serializable {
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "product_id")
    private Long productId;
}
