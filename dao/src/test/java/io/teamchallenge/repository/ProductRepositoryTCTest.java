package io.teamchallenge.repository;

import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.Product_;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
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

    @Test
    void findProductMinMaxPriceWithoutSpecificationTest() {
        var actual = productRepository.findProductMinMaxPrice(null);
        var expected = new ProductMinMaxPriceDto(BigDecimal.valueOf(19.99),BigDecimal.valueOf(599.99));
        assertEquals(expected,actual);
    }

    @Test
    void findProductMinMaxPriceWithSpecificationTest() {
        Specification<Product> specificationForBrands = (root, query, builder) ->
            root.get(Product_.brand).get("id").in(1L);
        Specification<Product> specificationForCategories = (root, query, builder) ->
            root.get(Product_.category).get("id").in(1L);

        var actual = productRepository.findProductMinMaxPrice(specificationForBrands.and(specificationForCategories));
        var expected = new ProductMinMaxPriceDto(BigDecimal.valueOf(19.99),BigDecimal.valueOf(19.99));
        assertEquals(expected,actual);
    }

    @Test
    void findAllProductIdsWithoutSpecificationTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(actual.getContent().size(),2);
        assertEquals(actual.getTotalPages(),2);
        assertEquals(actual.getPageable().getPageNumber(),0);
        assertEquals(actual.getTotalElements(),2);
    }

    @Test
    void findAllProductIdsWithSpecificationTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        Specification<Product> specificationForBrands = (root, query, builder) ->
            root.get(Product_.brand).get("id").in(1L);
        Specification<Product> specificationForCategories = (root, query, builder) ->
            root.get(Product_.category).get("id").in(1L);

        var actual = productRepository.findAllProductIds(
            specificationForBrands.and(specificationForCategories),pageable);
        assertEquals(actual.getContent().size(),1);
        assertEquals(actual.getTotalPages(),1);
        assertEquals(actual.getPageable().getPageNumber(),0);
        assertEquals(actual.getTotalElements(),1);
    }
}
