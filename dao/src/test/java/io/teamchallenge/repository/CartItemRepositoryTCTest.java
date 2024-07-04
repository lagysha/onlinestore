package io.teamchallenge.repository;

import io.teamchallenge.entity.cartitem.CartItemId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class CartItemRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void findCartItemIdsByUserIdTest() {
        var cartItemIdPage = cartItemRepository.findCartItemIdsByUserId(1L, Pageable.ofSize(2));
        assertFalse(cartItemIdPage.getContent().isEmpty());
        assertEquals(1L, cartItemIdPage.getContent().getFirst().getUserId());
    }

    @Test
    void findAllByIdWithImagesAndProductsTest() {
        var cartItems = cartItemRepository
            .findAllByIdWithImagesAndProducts(List.of(new CartItemId(1L, 2L), new CartItemId(2L, 1L))
                , Sort.by("createdAt"));

        TestTransaction.end();

        assertFalse(cartItems.isEmpty());
        assertEquals(1L, cartItems.getFirst().getProduct().getImages().size());
        LocalDateTime expected =
            LocalDateTime.parse("2024-05-10 14:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        assertEquals(expected, cartItems.getFirst().getCreatedAt());
    }
}
