package com.samuel.productservice.infrastructure.adapter.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.infrastructure.adapter.persistence.mapper.ProductPersistenceMapper;
import com.samuel.productservice.infrastructure.config.BaseContainersIntegrationTest;

import jakarta.persistence.EntityManager;

@Import({ ProductRepositoryImpl.class, ProductPersistenceMapper.class })
@DisplayName("ProductRepositoryImpl Integration Tests with Testcontainers")
class ProductRepositoryImplTest extends BaseContainersIntegrationTest {

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
            var product = createDummyProduct("FIND");
            var productId = product.getId();
            productRepository.save(product);

            // Flush/Clear to ensure that findById fetches from the actual database rather
            // than the Hibernate cache.
            entityManager.flush();
            entityManager.clear();

            // Act
            var foundProduct = productRepository.findById(productId);

            // Assert
            assertThat(foundProduct)
                    .isPresent()
                    .hasValueSatisfying(actualProduct -> {
                        assertThat(actualProduct.getId()).isEqualTo(product.getId());
                        assertThat(actualProduct.getSku()).isEqualTo(product.getSku());
                        assertThat(actualProduct.getName()).isEqualTo(product.getName());
                        assertThat(actualProduct.getStock()).isEqualByComparingTo(product.getStock());
                        assertThat(actualProduct.getCost()).isEqualByComparingTo(product.getCost());
                        assertThat(actualProduct.getPrice()).isEqualByComparingTo(product.getPrice());
                    });
        }

        @Test
        @DisplayName("Should return empty optional when product does not exist")
        void shouldReturnEmptyWhenIdNotFound() {
            // Act
            var foundProduct = productRepository.findById(UUID.randomUUID());

            // Assert
            assertThat(foundProduct).isEmpty();
        }

        @Test
        @DisplayName("Should retrieve an existing product by its unique SKU code")
        void shouldFindProductBySku() {
            // Arrange
            var product = createDummyProduct("FIND-SKU");
            var productSku = product.getSku();
            productRepository.save(product);

            // Flush/Clear to ensure that findById fetches from the actual database rather
            // than the Hibernate cache.
            entityManager.flush();
            entityManager.clear();

            // Act
            var foundProduct = productRepository.findBySku(productSku);

            // Assert
            assertThat(foundProduct)
                    .isPresent()
                    .hasValueSatisfying(actualProduct -> {
                        assertThat(actualProduct.getId()).isEqualTo(product.getId());
                        assertThat(actualProduct.getSku()).isEqualTo(product.getSku());
                        assertThat(actualProduct.getName()).isEqualTo(product.getName());
                        assertThat(actualProduct.getStock()).isEqualByComparingTo(product.getStock());
                        assertThat(actualProduct.getCost()).isEqualByComparingTo(product.getCost());
                        assertThat(actualProduct.getPrice()).isEqualByComparingTo(product.getPrice());
                    });
        }

        @Test
        @DisplayName("Should return empty optional when product SKU does not exist")
        void shouldReturnEmptyWhenSkuNotFound() {
            // Act
            var foundProduct = productRepository.findBySku("NON-EXISTENT-SKU-999");

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
            var product = createDummyProduct("SAVE");
            var productId = product.getId();

            // Act
            var savedProduct = productRepository.save(product);

            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getId()).isEqualTo(productId);

            var entityInDatabase = jpaProductRepository.findById(productId);
            assertThat(entityInDatabase)
                    .isPresent()
                    .hasValueSatisfying(entity -> {
                        assertThat(entity.getId()).isEqualTo(product.getId());
                        assertThat(entity.getSku()).isEqualTo(product.getSku());
                        assertThat(entity.getName()).isEqualTo(product.getName());
                        assertThat(entity.getStock()).isEqualByComparingTo(product.getStock());
                        assertThat(entity.getCost()).isEqualByComparingTo(product.getCost());
                        assertThat(entity.getPrice()).isEqualByComparingTo(product.getPrice());
                    });

            // Once persisted and reloaded from the database, the JPA record's `isNew` flag
            // should be `false`.
            assertThat(entityInDatabase.get().isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Updating Products")
    class UpdateOperations {

        @Test
        @DisplayName("Should update an existing product successfully modifying its database record")
        void shouldUpdateExistingProduct() {
            // Arrange
            // Creates and persists the original product in the database.
            var originalProduct = createDummyProduct("UPDATE");
            var productId = originalProduct.getId();
            productRepository.save(originalProduct);

            entityManager.flush();
            entityManager.clear();

            // Modifies the object's state using the rich domain's business method.
            var productToUpdate = productRepository.findById(productId).orElseThrow();
            productToUpdate.update(
                    "SKU-UPDATE-02", // New SKU
                    "Monitor Gamer Ultrawide", // New Name
                    BigDecimal.valueOf(5), // New Stock
                    BigDecimal.valueOf(500), // New Cost
                    BigDecimal.valueOf(900) // New Price
            );

            // Act - Executes the update that uses toUpdateEntity(isNew = false).
            var updatedProduct = productRepository.update(productToUpdate);

            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedProduct).isNotNull();
            assertThat(updatedProduct.getId()).isEqualTo(productId);
            assertThat(updatedProduct.getName()).isEqualTo("Monitor Gamer Ultrawide");

            // Physical Verification – Ensures that the changes were actually committed to
            // the database within the container.
            var entityInDatabase = jpaProductRepository.findById(productId);
            assertThat(entityInDatabase)
                    .isPresent()
                    .hasValueSatisfying(entity -> {
                        assertThat(entity.getId()).isEqualTo(updatedProduct.getId());
                        assertThat(entity.getSku()).isEqualTo(updatedProduct.getSku());
                        assertThat(entity.getName()).isEqualTo(updatedProduct.getName());
                        assertThat(entity.getStock()).isEqualByComparingTo(updatedProduct.getStock());
                        assertThat(entity.getCost()).isEqualByComparingTo(updatedProduct.getCost());
                        assertThat(entity.getPrice()).isEqualByComparingTo(updatedProduct.getPrice());
                        assertThat(entity.isNew()).isFalse(); // It remains false because it was an
                                                              // update.
                    });
        }
    }

    private Product createDummyProduct(String suffix) {
        return Product.create(
                "SKU-TEST-" + suffix,
                "Product Test " + suffix,
                BigDecimal.ONE,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(200));
    }
}