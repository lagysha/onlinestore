package io.teamchallenge.repository;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository
    extends JpaRepository<CartItem, CartItemId>, BaseJpaRepository<CartItem, CartItemId> {
    /**
     * Retrieves a page of cart item IDs for a specific user.
     *
     * @param userId   the ID of the user whose cart item IDs are to be retrieved
     * @param pageable the pagination information
     * @return a page of CartItemIds for the specified user
     */
    @Query(value = "select ci.id from CartItem ci where ci.user.id = :userId")
    Page<CartItemId> findCartItemIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Retrieves a list of cart items with their associated product and product images,
     * based on a list of cart item IDs.
     *
     * @param cartItemIds the list of cart item IDs to be retrieved
     * @return a list of CartItems with associated products and images
     */
    @EntityGraph(attributePaths = {"product.images", "product"})
    @Query(value = "select ci from CartItem ci where ci.id in :cartItemIds")
    List<CartItem> findAllByIdWithImagesAndProducts(@Param("cartItemIds") List<CartItemId> cartItemIds);
}
