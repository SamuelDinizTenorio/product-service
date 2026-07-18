package com.samuel.productservice.infrastructure.adapter.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.infrastructure.adapter.persistence.fixture.ProductEntityFixture;

@DisplayName("ProductPersistenceMapper Unit Tests")
class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    @Nested
    @DisplayName("toNewEntity Mapping Tests")
    class ToNewEntity {

        @Test
        @DisplayName("Should map domain Product into new ProductEntity persistence record")
        void shouldMapProductToNewProductEntity() {
            // Arrange
            final var product = ProductFixture.any();

            // Act
            final var entity = mapper.toNewEntity(product);

            // Assert
            assertThat(entity)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("sku", "isNew")
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(product);

            assertThat(entity.isNew()).isTrue();
            assertThat(entity.getSku()).isEqualTo(product.getSku().getValue());
        }

        @Test
        @DisplayName("Should return null when product is null")
        void shouldReturnNullWhenProductIsNull() {
            // Act
            final var entity = mapper.toNewEntity(null);

            // Assert
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("toUpdateEntity Mapping Tests")
    class ToUpdateEntity {

        @Test
        @DisplayName("Should map domain Product into existing ProductEntity persistence record")
        void shouldMapProductToUpdateProductEntity() {
            // Arrange
            final var existingId = UUID.randomUUID();
            final var product = ProductFixture.reconstituteWithId(existingId);

            // Act
            final var entity = mapper.toUpdateEntity(product);

            // Assert
            assertThat(entity)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("sku", "isNew")
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(product);

            assertThat(entity.isNew()).isFalse();
            assertThat(entity.getSku()).isEqualTo(product.getSku().getValue());
        }

        @Test
        @DisplayName("Should return null when product is null")
        void shouldReturnNullWhenProductIsNull() {
            // Act
            final var entity = mapper.toUpdateEntity(null);

            // Assert
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("toDomain Mapping Tests")
    class ToDomain {

        @Test
        @DisplayName("Should map ProductEntity into domain Product aggregate")
        void shouldMapProductEntityToDomainProductAggregate() {
            // Arrange
            final var expectedId = UUID.randomUUID();
            final var entity = ProductEntityFixture.createWithId(expectedId);

            // Act
            final var product = mapper.toDomain(entity);

            // Assert
            assertThat(product)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("sku")
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(entity);

            assertThat(product.getSku().getValue()).isEqualTo(entity.getSku());
        }

        @Test
        @DisplayName("Should return null when productEntity is null")
        void shouldReturnNullWhenProductEntityIsNull() {
            // Act
            final var product = mapper.toDomain(null);

            // Assert
            assertThat(product).isNull();
        }
    }
}
