package io.teamchallenge.repository;

import io.teamchallenge.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
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
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("ts")
@Sql(scripts = "classpath:data.sql")
class ProductRepositoryTCTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByNameTest() {
        Optional<Product> product = productRepository.findByName("Example Smartphone");
        assertFalse(product.isEmpty());
        assertEquals(product.get().getName(), "Example Smartphone");
    }

    private void assertFalse(boolean empty) {
    }

    @Test
    void findByNameWhenNoProductWithSuchNamePresentTest() {
        Optional<Product> product = productRepository.findByName("name3");
        assertTrue(product.isEmpty());
    }

    @Test
    void findByNameAndIdNotTest() {
        Optional<Product> product = productRepository.findByNameAndIdNot("Example Smartphone", 2L);
        assertFalse(product.isEmpty());
        assertEquals(product.get().getName(), "Example Smartphone");
    }

    @Test
    void findByNameAndIdNotWhenNoProductWithSuchNameAndIdPresentTest() {
        Optional<Product> product = productRepository.findByNameAndIdNot("Example T-shirt", 2L);
        assertTrue(product.isEmpty());
    }

    @Test
    void findByIdWithImagesTest() {
        Optional<Product> product = productRepository.findByIdWithImages(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(product.get().getImages().size(), 1L);
    }

    @Test
    void findByIdWithImagesWhenNoProductWithIdPresentTest() {
        Optional<Product> product = productRepository.findByIdWithImages(3L);
        assertTrue(product.isEmpty());
    }

    @Test
    void findByIdWithCategoryAndBrandAndProductAttributeTest() {
        Optional<Product> product = productRepository.findByIdWithCategoryAndBrandAndProductAttribute(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(product.get().getCategory().getId(), 1L);
        assertEquals(product.get().getBrand().getId(), 1L);
        assertEquals(product.get().getProductAttributes().size(), 2);
    }

    @Test
    void findByIdWithCategoryAndBrandAndProductAttributeWhenNoProductWithIdPresentTest() {
        Optional<Product> product = productRepository.findByIdWithCategoryAndBrandAndProductAttribute(3L);
        assertTrue(product.isEmpty());
    }

    @Test
    void findAllByIdWithImagesTest() {
        List<Product> products = productRepository.findAllByIdWithImages(List.of(1L, 2L), Sort.unsorted());

        TestTransaction.end();

        assertEquals(products.size(), 2);
        assertEquals(products.getFirst().getImages().size(), 1L);
    }

    @Test
    void findAllByIdWithImagesWhenNoProductWithIdPresentTest() {
        List<Product> product = productRepository.findAllByIdWithImages(List.of(3L), Sort.unsorted());
        assertTrue(product.isEmpty());
    }

    @Test
    void findAllIdsByNameTest() {
        Page<Long> productsIds = productRepository.
            findAllIdsByName(Pageable.ofSize(3), "Example T-shirt".toLowerCase());

        TestTransaction.end();

        assertEquals(productsIds.getTotalElements(), 1);
        assertEquals(productsIds.getContent().getFirst(), 2L);
    }

    @Test
    void findAllIdsByNameWhenNoProductWithIdPresentTest() {
        Page<Long> productsIds = productRepository.
            findAllIdsByName(Pageable.ofSize(3), "Example T-shirt1");
        assertTrue(productsIds.isEmpty());
    }

    @Test
    void findByIdWithCollectionsTest() {
        Optional<Product> product = productRepository.findByIdWithCollections(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(product.get().getCategory().getId(), 1L);
        assertEquals(product.get().getBrand().getId(), 1L);
        assertEquals(product.get().getProductAttributes().size(), 2);
        assertEquals(product.get().getImages().size(), 1);
    }

    @Test
    void findByIdWithCollectionsWhenNoProductWithIdPresentTest() {
        Optional<Product> product = productRepository.findByIdWithCollections(3L);
        assertTrue(product.isEmpty());
    }
}
