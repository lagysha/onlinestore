package io.teamchallenge.entity.attributes;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "attribute_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"attribute","productAttributes"})
@Builder
@EqualsAndHashCode(exclude = {"attribute","productAttributes"})
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id",nullable = false)
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeValue")
    @Setter(AccessLevel.PRIVATE)
    private List<ProductAttribute> productAttributes;


    /**
     * Adds a product attribute to the list of attributes associated with this product.
     * This method also sets the attribute value reference in the added product attribute.
     *
     * @param productAttribute The product attribute to add.
     */
    public void addProductAttribute(ProductAttribute productAttribute) {
        productAttributes.add(productAttribute);
        productAttribute.setAttributeValue(this);
    }

    /**
     * Removes a product attribute from the list of attributes associated with this product.
     * This method also removes the attribute value reference from the removed product attribute.
     *
     * @param productAttribute The product attribute to remove.
     */
    public void removeProductAttribute(ProductAttribute productAttribute) {
        productAttributes.remove(productAttribute);
        productAttribute.setAttributeValue(null);
    }

}
