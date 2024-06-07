package io.teamchallenge.repository;

import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    @Query("""
        SELECT r FROM Review r
        LEFT JOIN FETCH r.user u
        WHERE r.product.id = :productId
        """)
    Page<Review> findAllByProductId(Long productId, Pageable pageable);
}
