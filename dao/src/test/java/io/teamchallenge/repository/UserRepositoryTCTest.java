package io.teamchallenge.repository;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.User;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class UserRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserVOByEmailTest() {
        String email = "john@example.com";
        Optional<UserVO> userVOByEmail = userRepository.findUserVOByEmail(email);
        assertFalse(userVOByEmail.isEmpty());
        assertEquals(email, userVOByEmail.get().getEmail());
    }

    @Test
    void findUserByEmailTest() {
        String email = "john@example.com";
        Optional<User> userVOByEmail = userRepository.findUserByEmail(email);
        assertFalse(userVOByEmail.isEmpty());
        assertEquals(email, userVOByEmail.get().getEmail());
    }

    @Test
    void existsByEmailTest() {
        assertTrue(userRepository.existsByEmail("john@example.com"));
    }

    @Test
    void existsByPhoneNumberTest() {
        assertTrue(userRepository.existsByPhoneNumber("+1234567890"));
    }
}
