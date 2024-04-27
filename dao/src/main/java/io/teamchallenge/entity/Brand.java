package io.teamchallenge.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"products"})
@Builder
@EqualsAndHashCode(exclude = {"products"})
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand")
    @Setter(AccessLevel.PRIVATE)
    private List<Product> products = new ArrayList<>();

    /**
     * Adds a product to the list of products associated with this brand.
     * Additionally, sets the brand of the product to this brand.
     *
     * @param product The product to be added.
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setBrand(this);
    }

    /**
     * Removes a product from the list of products associated with this brand.
     * Additionally, sets the brand of the product to null.
     *
     * @param product The product to be removed.
     */
    public void removeProduct(Product product) {
        products.remove(product);
        product.setBrand(null);
    }
}
