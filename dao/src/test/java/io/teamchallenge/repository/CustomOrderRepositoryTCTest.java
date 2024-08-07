package io.teamchallenge.repository;

import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.teamchallenge.enumerated.DeliveryMethod.NOVA;
import static io.teamchallenge.enumerated.DeliveryStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
public class CustomOrderRepositoryTCTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllByFilterParametersTest() {
        OrderFilterDto orderFilterDto = OrderFilterDto.builder().isPaid(true)
            .createdAfter(LocalDateTime.of(2020,1,1,1,1))
            .createdBefore(LocalDateTime.of(2026,1,1,1,1))
            .deliveryMethods(List.of(NOVA))
            .statuses(List.of(COMPLETED))
            .totalLess(BigDecimal.valueOf(2000))
            .totalMore(BigDecimal.ZERO)
            .build();
        Pageable pageable = PageRequest.of(0,2, Sort.by("total"));
        Page<Order> page = orderRepository.findAllByFilterParameters(orderFilterDto, pageable);

        assertFalse(page.getContent().isEmpty());
        assertEquals(1, page.getContent().size());
    }
}
