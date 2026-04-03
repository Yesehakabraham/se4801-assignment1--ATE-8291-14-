// Student Number: [YOUR_STUDENT_NUMBER]
package com.shopwave;

import com.shopwave.model.Category;
import com.shopwave.model.Order;
import com.shopwave.model.OrderStatus;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        log.info("=== ShopWave: Loading seed data ===");

        Category electronics = categoryRepository.save(
                Category.builder().name("Electronics").description("Gadgets and devices").build());
        Category clothing = categoryRepository.save(
                Category.builder().name("Clothing").description("Apparel and fashion").build());

        productRepository.save(Product.builder()
                .name("Laptop Pro 15").description("High-performance laptop")
                .price(new BigDecimal("1299.99")).stock(25).category(electronics).build());
        productRepository.save(Product.builder()
                .name("Wireless Headphones").description("Noise-cancelling")
                .price(new BigDecimal("199.99")).stock(50).category(electronics).build());
        productRepository.save(Product.builder()
                .name("Running Shoes").description("Lightweight running shoes")
                .price(new BigDecimal("89.99")).stock(100).category(clothing).build());

        log.info("=== ShopWave: Seed data loaded successfully ===");
    }
}
