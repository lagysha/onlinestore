package io.teamchallenge.repository;

import io.teamchallenge.entity.attributes.Attribute;
import java.util.Optional;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class AttributeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private  AttributeRepository attributeRepository;

    @Test
    void findNameTest() {
        String name = "Size";
        Optional<Attribute> attribute = attributeRepository.findByName(name);
        assertFalse(attribute.isEmpty());
        assertEquals(name, attribute.get().getName());
    }
}
