package io.teamchallenge.entity.attributes;

import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.attributes.Attribute;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category","attribute"})
@Builder
@EqualsAndHashCode(exclude = {"category","attribute"})
public class CategoryAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id",nullable = false)
    private Attribute attribute;
}
