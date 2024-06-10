package io.teamchallenge.repository;

import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    /**
     * Retrieves a page of reviews for a specific product by its ID, including the associated user details.
     *
     * @param productId the ID of the product whose reviews are to be retrieved
     * @param pageable  the pagination information
     * @return a {@link Page} of {@link Review} entities for the specified product ID
     */
    @Query("SELECT r FROM Review r "
           + "LEFT JOIN FETCH r.user u "
           + "WHERE r.product.id = :productId")
    Page<Review> findAllByProductId(Long productId, Pageable pageable);
}
