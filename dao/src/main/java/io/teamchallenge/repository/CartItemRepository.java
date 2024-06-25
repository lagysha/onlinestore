package io.teamchallenge.repository;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing {@link CartItem} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
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
     * Finds all {@link CartItem} entities by their IDs with associated product images and products.
     *
     * @param cartItemIds the list of {@link CartItemId} to find the cart items by.
     * @param sort the sorting criteria for the result list.
     * @return a list of {@link CartItem} entities with associated product images and products.
     */
    @EntityGraph(attributePaths = {"product.images", "product"})
    @Query(value = "select ci from CartItem ci "
        + "left join fetch ci.product p "
        + "left join fetch p.images img "
        + "where ci.id in :cartItemIds "
        + "and img.order = 1")
    List<CartItem> findAllByIdWithImagesAndProducts(@Param("cartItemIds") List<CartItemId> cartItemIds, Sort sort);
}
