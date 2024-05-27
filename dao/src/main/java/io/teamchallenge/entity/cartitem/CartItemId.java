package io.teamchallenge.entity.cartitem;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@ToString
@EqualsAndHashCode
public class CartItemId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_id")
    private Long productId;
}
