package io.teamchallenge.repository;

import io.teamchallenge.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link Image} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("SELECT i FROM Image i left join i.product p where p.id in (:productIds)")
    List<Image> findAllByProductId(List<Long> productIds);
}
