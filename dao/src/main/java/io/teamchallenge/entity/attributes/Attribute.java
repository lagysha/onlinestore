package io.teamchallenge.entity.attributes;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"categoryAttributes"})
@Builder
@EqualsAndHashCode(exclude = {"categoryAttributes"})
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "attribute")
    @Setter(AccessLevel.PRIVATE)
    private List<CategoryAttribute> categoryAttributes = new ArrayList<>();

    @OneToMany(mappedBy = "attribute")
    @Setter(AccessLevel.PRIVATE)
    private List<AttributeValue> attributeValues = new ArrayList<>();


    /**
     * Adds a category attribute to the category.
     *
     * @param categoryAttribute The category attribute to be added.
     */
    public void addCategoryAttribute(CategoryAttribute categoryAttribute) {
        categoryAttributes.add(categoryAttribute);
        categoryAttribute.setAttribute(this);
    }

    /**
     * Removes a category attribute from the category.
     *
     * @param categoryAttribute The category attribute to be removed.
     */
    public void removeCategoryAttribute(CategoryAttribute categoryAttribute) {
        categoryAttributes.remove(categoryAttribute);
        categoryAttribute.setAttribute(null);
    }

    /**
     * Adds an attribute value to the list of values associated with this attribute.
     * This method also sets the attribute reference in the added value.
     *
     * @param attributeValue The attribute value to add.
     */
    public void addAttributeValue(AttributeValue attributeValue) {
        attributeValues.add(attributeValue);
        attributeValue.setAttribute(this);
    }

    /**
     * Removes an attribute value from the list of values associated with this attribute.
     * This method also removes the attribute reference from the removed value.
     *
     * @param attributeValue The attribute value to remove.
     */
    public void removeCategoryAttribute(AttributeValue attributeValue) {
        attributeValues.remove(attributeValue);
        attributeValue.setAttribute(null);
    }

}