package io.teamchallenge.entity;

import io.teamchallenge.entity.cartitem.CartItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category","brand","images","cartItems"})
@Builder
@EqualsAndHashCode(exclude = {"category","brand","images","cartItems"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_desc", nullable = false)
    private String shortDesc;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product")
    @Setter(AccessLevel.PRIVATE)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @Setter(AccessLevel.PRIVATE)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> characteristics;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String desc;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Adds an image to the product and sets the product for the image.
     *
     * @param image The image to add to the product.
     */
    public void addImage(Image image) {
        images.add(image);
        image.setProduct(this);
    }

    /**
     * Removes an image from the product and sets the product of the image to null.
     *
     * @param image The image to remove from the product.
     */
    public void removeImage(Image image) {
        images.remove(image);
        image.setProduct(null);
    }

    /**
     * Adds a cart item to the user's list of cart items.
     * Also sets this product as the product associated with the added cart item.
     *
     * @param cartItem The cart item to be added.
     */
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setProduct(this);
    }

    /**
     * Removes a cart item from the user's list of cart items.
     * Also removes the association of this product from the removed cart item.
     *
     * @param cartItem The cart item to be removed.
     */
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setProduct(null);
    }
}
