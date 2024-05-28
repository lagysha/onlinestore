package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.AttributeValue;
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
public class AttributeValueRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Test
    void findAllByIdInTest() {
        List<AttributeValue> attributes = attributeValueRepository.findAllByIdIn(List.of(1L, 2L));

        TestTransaction.end();

        assertEquals(attributes.size(), 2);
        assertEquals(attributes.getFirst().getAttribute().getId(), 1L);
    }

    @Test
    void findAllByIdInWhenNonExistingIdsTest() {
        List<AttributeValue> product = attributeValueRepository.findAllByIdIn(List.of(7L));

        assertTrue(product.isEmpty());
    }
}
