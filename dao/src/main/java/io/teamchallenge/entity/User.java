package io.teamchallenge.entity;

import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.enumerated.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"address","orders","carItems"})
@EqualsAndHashCode(exclude = {"address","orders","carItems"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, name = "phone_number")
    private String phoneNumber;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "user")
    private List<CartItem> carItems = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "refresh_token_key", nullable = false)
    private String refreshTokenKey;

    /**
     * Sets the address for this user and updates the user reference in the address object.
     *
     * @param address The address to set.
     */
    public void setAddress(Address address) {
        this.address = address;
        address.setUser(this);
    }

    /**
     * Adds an order to the user's list of orders.
     * Also sets the user for the added order.
     *
     * @param order The order to be added.
     */
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    /**
     * Removes an order from the user's list of orders.
     * Also sets the user of the removed order to null.
     *
     * @param order The order to be removed.
     */
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }

    /**
     * Adds a cart item to the user's list of cart items.
     * Also sets the user for the added cart item.
     *
     * @param cartItem The cart item to be added.
     */
    public void addCarItem(CartItem cartItem) {
        carItems.add(cartItem);
        cartItem.setUser(this);
    }

    /**
     * Removes a cart item from the user's list of cart items.
     * Also sets the user of the removed cart item to null.
     *
     * @param cartItem The cart item to be removed.
     */
    public void removeCarItem(CartItem cartItem) {
        carItems.remove(cartItem);
        cartItem.setUser(null);
    }
}
