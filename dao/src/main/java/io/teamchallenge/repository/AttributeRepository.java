package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.Attribute;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Attribute} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface AttributeRepository extends JpaRepository<Attribute,Long> {
    /**
     * Finds an attribute by its name.
     *
     * @param name the name of the attribute to find
     * @return an {@link Optional} containing the found attribute, or {@link Optional#empty()} if no attribute with the given name is found
     */
    Optional<Attribute> findByName(String name);
}
