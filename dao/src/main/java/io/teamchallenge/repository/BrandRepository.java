package io.teamchallenge.repository;

import io.teamchallenge.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Brand} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface BrandRepository extends JpaRepository<Brand,Long> {
}
