package com.samuel.productservice.infrastructure.adapter.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.samuel.productservice.core.product.model.Product;
import com.samuel.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import com.samuel.productservice.infrastructure.config.BaseContainersIntegrationTest;

import jakarta.persistence.EntityManager;

@Import({ ProductRepositoryImpl.class, ProductMapper.class })
@DisplayName("ProductRepositoryImpl Integration Tests with Testcontainers")
public class ProductRepositoryImplTest extends BaseContainersIntegrationTest {

    @Autowired
    private ProductRepositoryImpl productRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("Finding Products")
    class FindOperations {

        @Test
        @DisplayName("Should retrieve an existing product by its unique identifier")
        void shouldFindProductById() {
            // Arrange
            var productId = UUID.randomUUID();
            var product = Product.reconstitute(productId, "SKU-TC-02", "Teclado Mecânico", BigDecimal.ONE,
                    BigDecimal.valueOf(50), BigDecimal.valueOf(120));

            // Persiste o cenário inicial
            productRepository.save(product);

            // Act
            var foundProduct = productRepository.findById(productId);

            // Assert
            assertThat(foundProduct).isPresent();
            assertThat(foundProduct.get().getSku()).isEqualTo("SKU-TC-02");
            assertThat(foundProduct.get().getName()).isEqualTo("Teclado Mecânico");
        }

        @Test
        @DisplayName("Should return empty optional when product does not exist")
        void shouldReturnEmptyWhenIdNotFound() {
            // Act
            var foundProduct = productRepository.findById(UUID.randomUUID());

            // Assert
            assertThat(foundProduct).isEmpty();
        }
    }

    @Nested
    @DisplayName("Saving Products")
    class SaveOperations {

        @Test
        @DisplayName("Should persist a new product successfully and handle isNew lifecycle properly")
        void shouldPersistNewProduct() {
            // Arrange
            var productId = UUID.randomUUID();
            var product = Product.reconstitute(
                    productId,
                    "SKU-TC-01",
                    "Mouse Gamer Testcontainers",
                    BigDecimal.TEN,
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(200));

            // Act
            var savedProduct = productRepository.save(product);

            // Forces the recording and clears the first-level cache
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getId()).isEqualTo(productId);

            // Physical verification on the actual database running in the container
            var entityInDatabase = jpaProductRepository.findById(productId);
            assertThat(entityInDatabase).isPresent();
            assertThat(entityInDatabase.get().getName()).isEqualTo("Mouse Gamer Testcontainers");
            assertThat(entityInDatabase.get().isNew()).isFalse();
        }
    }
}
