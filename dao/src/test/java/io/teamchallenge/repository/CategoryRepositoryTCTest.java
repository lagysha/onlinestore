package io.teamchallenge.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
public class CategoryRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAllAttributeAttributeValueByCategoryTest() {
        var expected = categoryRepository.findAllAttributeAttributeValueByCategory(1L).toList();
        assertEquals(2, expected.size());
        assertEquals(1L, expected.getFirst().getAttributeId());
        assertEquals(1L, expected.getFirst().getAttributeValueId());
        assertEquals(3L, expected.get(1).getAttributeId());
        assertEquals(5L, expected.get(1).getAttributeValueId());
    }
}
