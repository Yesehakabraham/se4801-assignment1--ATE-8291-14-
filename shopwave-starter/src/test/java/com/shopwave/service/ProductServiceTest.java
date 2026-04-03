// Student Number: [ATE/8291/14]
// Student Name : YESEHAK ABRAHAM
package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.CategoryNotFoundException;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Electronics").build();

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .category(category)
                .build();

        productDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
    }

    // ── createProduct — happy path ────────────────────────────────────────────
    @Test
    @DisplayName("createProduct: returns ProductDTO when category exists")
    void createProduct_happyPath() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Laptop")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .categoryId(1L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request, category)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getCategoryId()).isEqualTo(1L);
        verify(productRepository).save(product);
    }

    // ── createProduct — category not found ───────────────────────────────────
    @Test
    @DisplayName("createProduct: throws CategoryNotFoundException when category missing")
    void createProduct_categoryNotFound() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Laptop")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .categoryId(99L)
                .build();

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).save(any());
    }

    // ── getProductById — not found ────────────────────────────────────────────
    @Test
    @DisplayName("getProductById: throws ProductNotFoundException for missing id")
    void getProductById_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ── updateStock — negative result ─────────────────────────────────────────
    @Test
    @DisplayName("updateStock: throws IllegalArgumentException when stock would go negative")
    void updateStock_goesNegative() {
        product.setStock(2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.updateStock(1L, -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");
    }

    // ── updateStock — happy path ──────────────────────────────────────────────
    @Test
    @DisplayName("updateStock: updates stock correctly for valid delta")
    void updateStock_happyPath() {
        product.setStock(10);
        Product updated = Product.builder().id(1L).name("Laptop")
                .price(new BigDecimal("999.99")).stock(7).category(category).build();
        ProductDTO updatedDTO = ProductDTO.builder().id(1L).stock(7).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(updated);
        when(productMapper.toDTO(updated)).thenReturn(updatedDTO);

        ProductDTO result = productService.updateStock(1L, -3);

        assertThat(result.getStock()).isEqualTo(7);
    }
}
