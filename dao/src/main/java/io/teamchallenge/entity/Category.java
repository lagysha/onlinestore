package io.teamchallenge.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"products"})
@EqualsAndHashCode(exclude = {"products"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String desc;

    @OneToMany(mappedBy = "category")
    @Setter(AccessLevel.PRIVATE)
    private List<Product> products = new ArrayList<>();

    /**
     * Adds a product to the category and sets the category for the product.
     *
     * @param product The product to add to the category.
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    /**
     * Removes a product from the category and sets the category of the product to null.
     *
     * @param product The product to remove from the category.
     */
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
}
