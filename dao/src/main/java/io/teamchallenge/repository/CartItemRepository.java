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

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId>, BaseJpaRepository<CartItem,CartItemId> {


    @Query(value = "select ci.id from CartItem ci " +
        " where ci.user.id = :userId")
    Page<CartItemId> findCartItemIdsByUserId(Long userId, Pageable pageable);
    @EntityGraph(attributePaths = {"product.images","product"})
    @Query(value = "select ci from CartItem ci where ci.id in :cartItemIds")
    List<CartItem> findAllByIdWithImagesAndProducts(@Param("cartItemIds") List<CartItemId> cartItemIds);


}
