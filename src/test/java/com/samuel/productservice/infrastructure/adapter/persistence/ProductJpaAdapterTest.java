package com.samuel.productservice.infrastructure.adapter.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.infrastructure.adapter.persistence.mapper.ProductPersistenceMapper;
import com.samuel.productservice.infrastructure.config.BaseContainersIntegrationTest;

import jakarta.persistence.EntityManager;

@Import({ ProductJpaAdapter.class, ProductPersistenceMapper.class })
@DisplayName("ProductRepositoryImpl Integration Tests with Testcontainers")
class ProductJpaAdapterTest extends BaseContainersIntegrationTest {

    @Autowired
    private ProductJpaAdapter productJpaAdapter;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("Finding Products")
    class FindOperations {

        @Test
        @DisplayName("Should retrieve an existing product by its unique identifier")
        void shouldFindProductById() {
            // Arrange
            var product = ProductFixture.withSku("REPO-FIND-ID");
            var productId = product.getId();
            productJpaAdapter.save(product);

            // Flush/Clear to ensure that findById fetches from the actual database rather
            // than the Hibernate cache.
            entityManager.flush();
            entityManager.clear();

            // Act
            var foundProduct = productJpaAdapter.findById(productId);

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
            var foundProduct = productJpaAdapter.findById(UUID.randomUUID());

            // Assert
            assertThat(foundProduct).isEmpty();
        }

        @Test
        @DisplayName("Should retrieve an existing product by its unique SKU code")
        void shouldFindProductBySku() {
            // Arrange
            var product = ProductFixture.withSku("REPO-FIND-SKU");
            var productSku = product.getSku();
            productJpaAdapter.save(product);

            // Flush/Clear to ensure that findById fetches from the actual database rather
            // than the Hibernate cache.
            entityManager.flush();
            entityManager.clear();

            // Act
            var foundProduct = productJpaAdapter.findBySku(productSku);

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
            var foundProduct = productJpaAdapter.findBySku("NON-EXISTENT-SKU-999");

            // Assert
            assertThat(foundProduct).isEmpty();
        }

        @Test
        @DisplayName("Should retrieve a paginated collection of reconstituted product aggregates")
        void shouldRetrievePaginatedProducts() {
            // Arrange
            var product1 = ProductFixture.withSku("REPO-PAGE-1");
            var product2 = ProductFixture.withSku("REPO-PAGE-2");
            productJpaAdapter.save(product1);
            productJpaAdapter.save(product2);

            entityManager.flush();
            entityManager.clear();

            final var pageable = PageRequest.of(0, 10);

            // Act
            final var actualPage = productJpaAdapter.findAll(pageable);

            // Assert
            assertThat(actualPage)
                    .isNotNull()
                    .isNotEmpty();

            // Ensures that domain aggregates are persisted correctly and reconstituted
            assertThat(actualPage.getContent())
                    .extracting(Product::getSku)
                    .contains(product1.getSku(), product2.getSku());
        }

        @Test
        @DisplayName("Should return an empty page when querying a page index out of bounds")
        void shouldReturnEmptyPageWhenIndexOutOfBounds() {
            // Arrange - Ensures at least one item so the table is not completely empty.
            var product = ProductFixture.withSku("REPO-EMPTY-PAGE");
            productJpaAdapter.save(product);

            entityManager.flush();
            entityManager.clear();

            // Requests a very distant page (e.g., page 50) where we know there
            // will be no data.
            final var pageable = PageRequest.of(50, 10);

            // Act
            final var actualPage = productJpaAdapter.findAll(pageable);

            // Assert
            assertThat(actualPage)
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Saving Products")
    class SaveOperations {

        @Test
        @DisplayName("Should persist a new product successfully and handle isNew lifecycle properly")
        void shouldPersistNewProduct() {
            // Arrange
            var product = ProductFixture.withSku("REPO-SAVE");
            var productId = product.getId();

            // Act
            var savedProduct = productJpaAdapter.save(product);

            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getId()).isEqualTo(productId);

            var entityInDatabase = productJpaRepository.findById(productId);
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
            var originalProduct = ProductFixture.withSku("REPO-UPDATE");
            var productId = originalProduct.getId();
            productJpaAdapter.save(originalProduct);

            entityManager.flush();
            entityManager.clear();

            // Modifies the object's state using the rich domain's business method.
            var productToUpdate = productJpaAdapter.findById(productId).orElseThrow();
            productToUpdate.update(
                    "SKU-UPDATE-02", // New SKU
                    "Monitor Gamer Ultrawide", // New Name
                    BigDecimal.valueOf(5), // New Stock
                    BigDecimal.valueOf(500), // New Cost
                    BigDecimal.valueOf(900) // New Price
            );

            // Act - Executes the update that uses toUpdateEntity(isNew = false).
            var updatedProduct = productJpaAdapter.update(productToUpdate);

            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedProduct).isNotNull();
            assertThat(updatedProduct.getId()).isEqualTo(productId);
            assertThat(updatedProduct.getName()).isEqualTo("Monitor Gamer Ultrawide");

            // Physical Verification – Ensures that the changes were actually committed to
            // the database within the container.
            var entityInDatabase = productJpaRepository.findById(productId);
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

    @Nested
    @DisplayName("Deleting Products")
    class DeleteOperations {

        @Test
        @DisplayName("Should physically remove an existing product from the database")
        void shouldDeleteExistingProduct() {
            // Arrange - Creates and persists the product in the container's database.
            var product = ProductFixture.withSku("REPO-DELETE");
            var productId = product.getId();
            productJpaAdapter.save(product);

            entityManager.flush();
            entityManager.clear();

            // Act
            productJpaAdapter.deleteById(productId);

            // Synchronizes the persistence context to ensure the deletion has been
            // committed to the physical database.
            entityManager.flush();
            entityManager.clear();

            // Assert
            var deletedProductInDatabase = productJpaRepository.findById(productId);
            assertThat(deletedProductInDatabase).isEmpty();
        }

        @Test
        @DisplayName("Should execute safely without throwing exceptions when the product ID does not exist")
        void shouldHandleDeletionSafelyWhenIdDoesNotExist() {
            // Arrange
            var nonExistentId = UUID.randomUUID();

            // Act & Assert
            assertDoesNotThrow(() -> productJpaAdapter.deleteById(nonExistentId));
        }
    }
}
