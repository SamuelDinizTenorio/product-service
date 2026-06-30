package com.samuel.productservice.infrastructure.adapter.persistence.mapper;

import com.samuel.productservice.core.product.model.Product;
import com.samuel.productservice.infrastructure.adapter.persistence.entity.ProductEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductMapper Unit Tests")
class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapper();

    @Nested
    @DisplayName("toNewEntity Mapping Tests")
    class ToNewEntity {

        @Test
        @DisplayName("Should map domain Product to ProductEntity flagged as isNew=true")
        void shouldMapToNewEntity() {
            // Arrange
            final var product = Product.create(
                    "SKU-123",
                    "Teclado Mecânico",
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(150.00),
                    BigDecimal.valueOf(299.90));

            // Act
            final var entity = productMapper.toNewEntity(product);

            // Assert
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(product.getId());
            assertThat(entity.getSku()).isEqualTo(product.getSku());
            assertThat(entity.getName()).isEqualTo(product.getName());
            assertThat(entity.getStock()).isEqualTo(product.getStock());
            assertThat(entity.getCost()).isEqualTo(product.getCost());
            assertThat(entity.getPrice()).isEqualTo(product.getPrice());
            assertThat(entity.isNew()).isTrue();
        }

        @Test
        @DisplayName("Should return null when source product is null")
        void shouldReturnNullWhenProductIsNull() {
            // Act
            final var entity = productMapper.toNewEntity(null);

            // Assert
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("toUpdateEntity Mapping Tests")
    class ToUpdateEntity {

        @Test
        @DisplayName("Should map domain Product to ProductEntity flagged as isNew=false")
        void shouldMapToUpdateEntity() {
            // Arrange
            final var existingId = UUID.randomUUID();
            final var product = Product.reconstitute(
                    existingId,
                    "SKU-123",
                    "Teclado Mecânico",
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(150.00),
                    BigDecimal.valueOf(299.90));

            // Act
            final var entity = productMapper.toUpdateEntity(product);

            // Assert
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(product.getId());
            assertThat(entity.getSku()).isEqualTo(product.getSku());
            assertThat(entity.getName()).isEqualTo(product.getName());
            assertThat(entity.getStock()).isEqualTo(product.getStock());
            assertThat(entity.getCost()).isEqualTo(product.getCost());
            assertThat(entity.getPrice()).isEqualTo(product.getPrice());
            assertThat(entity.isNew()).isFalse();
        }

        @Test
        @DisplayName("Should return null when source product is null")
        void shouldReturnNullWhenProductIsNull() {
            // Act
            final var entity = productMapper.toUpdateEntity(null);

            // Assert
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("toDomain Mapping Tests")
    class ToDomain {

        @Test
        @DisplayName("Should reconstitute ProductEntity into domain Product aggregate")
        void shouldMapToDomain() {
            // Arrange
            final var expectedId = UUID.randomUUID();
            final var entity = ProductEntity.create(
                    expectedId,
                    "SKU-123",
                    "Teclado Mecânico",
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(150.00),
                    BigDecimal.valueOf(299.90),
                    false);

            // Act
            final var product = productMapper.toDomain(entity);

            // Assert
            assertThat(product).isNotNull();
            assertThat(product.getId()).isEqualTo(entity.getId());
            assertThat(product.getSku()).isEqualTo(entity.getSku());
            assertThat(product.getName()).isEqualTo(entity.getName());
            assertThat(product.getStock()).isEqualTo(entity.getStock());
            assertThat(product.getCost()).isEqualTo(entity.getCost());
            assertThat(product.getPrice()).isEqualTo(entity.getPrice());
        }

        @Test
        @DisplayName("Should return null when source entity is null")
        void shouldReturnNullWhenEntityIsNull() {
            // Act
            final var product = productMapper.toDomain(null);

            // Assert
            assertThat(product).isNull();
        }
    }
}
