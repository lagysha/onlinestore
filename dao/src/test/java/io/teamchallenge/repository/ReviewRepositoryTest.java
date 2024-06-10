package io.teamchallenge.repository;

import io.teamchallenge.entity.reviews.Review;
import io.teamchallenge.entity.reviews.ReviewId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class ReviewRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void findAllByProductIdTest() {
        Page<Review> allByProductId = reviewRepository.findAllByProductId(1L, PageRequest.of(0, 1));

        Review expected = Review.builder()
            .text("test text1")
            .rate((short) 1)
            .createdAt(LocalDateTime.parse("2024-05-11T14:00"))
            .id(ReviewId.builder()
                .userId(1L)
                .productId(1L)
                .build()).build();

        assertEquals(1, allByProductId.getSize());
        assertEquals(2, allByProductId.getTotalElements());
        assertEquals(expected, allByProductId.getContent().getFirst());
    }
}
