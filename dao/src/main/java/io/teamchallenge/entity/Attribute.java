package io.teamchallenge.entity;

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
}