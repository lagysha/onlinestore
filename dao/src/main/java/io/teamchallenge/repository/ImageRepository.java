package io.teamchallenge.repository;

import io.teamchallenge.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Image} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface ImageRepository extends JpaRepository<Image,Long> {
}
