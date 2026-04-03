// Student Number: [ATE/8291/14]
// Student Name : YESEHAK ABRAHAM
package com.shopwave.repository;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Category electronics;

    @BeforeEach
    void setUp() {
        electronics = entityManager.persistAndFlush(
                Category.builder().name("Electronics").description("Tech stuff").build());

        entityManager.persistAndFlush(Product.builder()
                .name("Apple MacBook Pro").price(new BigDecimal("1999.99"))
                .stock(5).category(electronics).build());

        entityManager.persistAndFlush(Product.builder()
                .name("Apple iPhone 15").price(new BigDecimal("999.99"))
                .stock(20).category(electronics).build());

        entityManager.persistAndFlush(Product.builder()
                .name("Samsung Galaxy S24").price(new BigDecimal("849.99"))
                .stock(15).category(electronics).build());

        entityManager.persistAndFlush(Product.builder()
                .name("Desk Lamp").price(new BigDecimal("29.99"))
                .stock(100).category(electronics).build());
    }

    // ── findByNameContainingIgnoreCase ────────────────────────────────────────
    @Test
    @DisplayName("findByNameContainingIgnoreCase: returns products matching keyword regardless of case")
    void findByNameContainingIgnoreCase_matchesCaseInsensitive() {
        List<Product> results = productRepository.findByNameContainingIgnoreCase("apple");

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Product::getName)
                .containsExactlyInAnyOrder("Apple MacBook Pro", "Apple iPhone 15");
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase: returns empty list when no match")
    void findByNameContainingIgnoreCase_noMatch() {
        List<Product> results = productRepository.findByNameContainingIgnoreCase("xyz_no_match");
        assertThat(results).isEmpty();
    }

    // ── findByPriceLessThanEqual ──────────────────────────────────────────────
    @Test
    @DisplayName("findByPriceLessThanEqual: returns products within price range")
    void findByPriceLessThanEqual_returnsCorrectProducts() {
        List<Product> results = productRepository
                .findByPriceLessThanEqual(new BigDecimal("999.99"));

        assertThat(results).hasSize(3); // iPhone, Galaxy, Lamp
        assertThat(results).extracting(Product::getName)
                .doesNotContain("Apple MacBook Pro");
    }

    // ── findByCategoryId ──────────────────────────────────────────────────────
    @Test
    @DisplayName("findByCategoryId: returns all products for a given category")
    void findByCategoryId_returnsAllInCategory() {
        List<Product> results = productRepository.findByCategoryId(electronics.getId());
        assertThat(results).hasSize(4);
    }

    // ── findTopByOrderByPriceDesc ─────────────────────────────────────────────
    @Test
    @DisplayName("findTopByOrderByPriceDesc: returns the most expensive product")
    void findTopByOrderByPriceDesc_returnsMostExpensive() {
        Optional<Product> result = productRepository.findTopByOrderByPriceDesc();

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Apple MacBook Pro");
    }
}
