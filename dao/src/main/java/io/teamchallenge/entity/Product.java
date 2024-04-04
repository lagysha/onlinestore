package io.teamchallenge.entity;

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
@ToString(exclude = {"category"})
@Builder
@EqualsAndHashCode(exclude = {"category"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_desc", nullable = false)
    private String shortDesc;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    @Setter(AccessLevel.PRIVATE)
    private List<Image> images = new ArrayList<>();

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
}
