package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.ProductAttribute;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class ProductAttributeRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Test
    void findAllByIdInTest() {
        List<ProductAttribute> attributes = productAttributeRepository.findAllByIdIn(List.of(1L, 2L));

        TestTransaction.end();

        assertEquals(attributes.size(), 2);
        assertEquals(attributes.getFirst().getAttributeValue().getId(), 1L);
        assertEquals(attributes.getFirst().getAttributeValue().getAttribute().getId(), 1L);
    }

    @Test
    void findAllByIdInWhenNonExistingIdsTest() {
        List<ProductAttribute> attributes = productAttributeRepository.findAllByIdIn(List.of(7L));

        assertTrue(attributes.isEmpty());
    }
}
