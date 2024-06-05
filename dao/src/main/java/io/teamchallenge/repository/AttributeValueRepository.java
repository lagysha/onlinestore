package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.AttributeValue;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Niktia Malov
 */
public interface AttributeValueRepository extends JpaRepository<AttributeValue,Long> {
    /**
     * Retrieves a list of AttributeValues by their IDs, eagerly fetching the associated attribute.
     *
     * @param ids The list of IDs of the AttributeValues to retrieve.
     * @return A list of AttributeValues with the associated attribute eagerly fetched.
     */
    @EntityGraph(attributePaths = {"attribute"})
    List<AttributeValue> findAllByIdIn(List<Long> ids);
}
