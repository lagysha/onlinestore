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

import static io.teamchallenge.repository.ProductRepository.Specs.byAttributeValuesIds;
import static io.teamchallenge.repository.ProductRepository.Specs.byBrandIds;
import static io.teamchallenge.repository.ProductRepository.Specs.byCategoryId;
import static io.teamchallenge.repository.ProductRepository.Specs.byName;
import static io.teamchallenge.repository.ProductRepository.Specs.byPriceRange;
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
        assertEquals("Example Smartphone", product.get().getName());
    }

    @Test
    void findByNameAndIdNotTest() {
        Optional<Product> product = productRepository.findByNameAndIdNot("Example Smartphone", 2L);
        assertFalse(product.isEmpty());
        assertEquals("Example Smartphone", product.get().getName());
    }

    @Test
    void findByIdWithImagesTest() {
        Optional<Product> product = productRepository.findByIdWithImages(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(1L, product.get().getImages().size());
    }

    @Test
    void findByIdWithReviewsTest() {
        Optional<Product> product = productRepository.findByIdWithReviews(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(1L, product.get().getReviews().size());
    }

    @Test
    void findByIdWithCategoryAndBrandAndProductAttributeTest() {
        Optional<Product> product = productRepository.findByIdWithCategoryAndBrandAndProductAttribute(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(1L, product.get().getCategory().getId());
        assertEquals(1L, product.get().getBrand().getId());
        assertEquals(2, product.get().getProductAttributes().size());
    }

    @Test
    void findAllByIdWithImagesTest() {
        List<Product> products = productRepository.findAllByIdWithImages(List.of(1L, 2L));

        TestTransaction.end();

        assertEquals(2, products.size());
        assertEquals(1L, products.getFirst().getImages().size());
    }

    @Test
    void findByIdWithCollectionsTest() {
        Optional<Product> product = productRepository.findByIdWithCollections(1L);

        TestTransaction.end();

        assertFalse(product.isEmpty());
        assertEquals(1L, product.get().getCategory().getId());
        assertEquals(1L, product.get().getBrand().getId());
        assertEquals(2, product.get().getProductAttributes().size());
        assertEquals(1, product.get().getImages().size());
    }

    @Test
    void findAllByIdsWithReviewsTest() {
        List<Product> products = productRepository.findAllByIdWithReviews(List.of(1L, 2L));

        TestTransaction.end();

        assertEquals(2, products.size());
        assertEquals(1L, products.getFirst().getReviews().getFirst().getId().getUserId());
    }

    @Test
    void findByIdsWithCollectionsTest() {
        List<Product> products = productRepository.findByIdsWithCollections(List.of(1L, 2L));

        TestTransaction.end();

        assertFalse(products.isEmpty());
        assertEquals(1L, products.getFirst().getCategory().getId());
        assertEquals(1L, products.getFirst().getBrand().getId());
        assertEquals(1, products.getFirst().getImages().size());
        assertEquals(1, products.getFirst().getReviews().size());
    }

    @Test
    void findProductMinMaxPriceWithoutSpecificationTest() {
        var actual = productRepository.findProductMinMaxPrice(null);
        var expected = new ProductMinMaxPriceDto(BigDecimal.valueOf(19.99), BigDecimal.valueOf(599.99));
        assertEquals(expected, actual);
    }

    @Test
    void findProductMinMaxPriceWithSpecificationTest() {
        Specification<Product> specificationForBrands = (root, query, builder) ->
            root.get(Product_.brand).get("id").in(1L);
        Specification<Product> specificationForCategories = (root, query, builder) ->
            root.get(Product_.category).get("id").in(1L);

        var actual = productRepository.findProductMinMaxPrice(specificationForBrands.and(specificationForCategories));
        var expected = new ProductMinMaxPriceDto(BigDecimal.valueOf(599.99), BigDecimal.valueOf(599.99));
        assertEquals(expected, actual);
    }

    @Test
    void findAllProductIdsWithoutSpecificationTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(1, actual.getContent().size());
        assertEquals(2, actual.getTotalPages());
        assertEquals(0, actual.getPageable().getPageNumber());
        assertEquals(2, actual.getTotalElements());
    }

    @Test
    void findAllProductIdsOrderByPriceAscTest() {
        PageRequest pageable = PageRequest.of(0, 2,Sort.by(Sort.Order.asc("price")));
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals(2L, actual.getContent().getFirst());
        assertEquals(1L, actual.getContent().get(1));
    }

    @Test
    void findAllProductIdsOrderByPriceDescTest() {
        PageRequest pageable = PageRequest.of(0, 2,Sort.by(Sort.Order.desc("price")));
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals(1L, actual.getContent().getFirst());
        assertEquals(2L, actual.getContent().get(1));
    }

    @Test
    void findAllProductIdsOrderByPopularityTest() {
        PageRequest pageable = PageRequest.of(0, 2,Sort.by(Sort.Order.by("popularity")));
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals(2L, actual.getContent().getFirst());
        assertEquals(1L, actual.getContent().get(1));
    }

    @Test
    void findAllProductIdsOrderByRatingTest() {
        PageRequest pageable = PageRequest.of(0, 2,Sort.by(Sort.Order.desc("rating")));
        var actual = productRepository.findAllProductIds(null, pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals(2L, actual.getContent().getFirst());
        assertEquals(1L, actual.getContent().get(1));
    }

    @Test
    void findAllProductIdsWithSpecificationTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        Specification<Product> specificationForBrands = (root, query, builder) ->
            root.get(Product_.brand).get("id").in(1L);
        Specification<Product> specificationForCategories = (root, query, builder) ->
            root.get(Product_.category).get("id").in(1L);

        var actual = productRepository.findAllProductIds(
            specificationForBrands.and(specificationForCategories), pageable);
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalPages());
        assertEquals(0, actual.getPageable().getPageNumber());
        assertEquals(1, actual.getTotalElements());
    }

    @Test
    void byNameTest() {
        String productName = "E";
        Specification<Product> specification = byName(productName);
        var actual = productRepository.findAll(specification);

        assertEquals(2, actual.size());
        assertEquals("Example Smartphone", actual.getFirst().getName());
    }

    @Test
    void byPriceTest() {
        BigDecimal from = BigDecimal.valueOf(1L);
        BigDecimal to = BigDecimal.valueOf(100L);
        Specification<Product> specification = byPriceRange(from, to);
        var actual = productRepository.findAll(specification);

        assertEquals(1, actual.size());
        assertTrue(
            from.compareTo(actual.getFirst().getPrice()) <= 0 &&
                to.compareTo(actual.getFirst().getPrice()) >= 0);
    }

    @Test
    void byBrandIdsTest() {
        List<Long> brandIds = List.of(1L);
        Specification<Product> specification = byBrandIds(brandIds);
        var actual = productRepository.findAll(specification);

        assertEquals(1, actual.size());
        assertEquals("Example Smartphone", actual.getFirst().getName());
    }

    @Test
    void byCategoryIdsTest() {
        Long categoriesIds = 1L;
        Specification<Product> specification = byCategoryId(categoriesIds);
        var actual = productRepository.findAll(specification);

        assertEquals(1, actual.size());
        assertEquals("Example Smartphone", actual.getFirst().getName());
    }

    @Test
    void byAttributeValuesIdsTest() {
        List<Long> attributeValuesIds = List.of(1L);
        Specification<Product> specification = byAttributeValuesIds(attributeValuesIds);
        var actual = productRepository.findAll(specification);

        assertEquals(1, actual.size());
        assertEquals("Example Smartphone", actual.getFirst().getName());
    }
}
