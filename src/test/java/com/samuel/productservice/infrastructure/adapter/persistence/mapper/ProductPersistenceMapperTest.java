package com.samuel.productservice.infrastructure.adapter.persistence.mapper;

import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.infrastructure.adapter.persistence.fixture.ProductEntityFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductPersistenceMapper Unit Tests")
class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    @Nested
    @DisplayName("toNewEntity Mapping Tests")
    class ToNewEntity {

        @Test
        @DisplayName("Should map domain Product to ProductEntity flagged as isNew=true")
        void shouldMapToNewEntity() {
            // Arrange
            final var product = ProductFixture.any();

            // Act
            final var entity = mapper.toNewEntity(product);

            // Assert
            assertThat(entity)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("isNew") // isNew does not exist in the Domain, so we ignore it.
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(product);

            assertThat(entity.isNew()).isTrue();
        }

        @Test
        @DisplayName("Should return null when source product is null")
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
        @DisplayName("Should map domain Product to ProductEntity flagged as isNew=false")
        void shouldMapToUpdateEntity() {
            // Arrange
            final var existingId = UUID.randomUUID();
            final var product = ProductFixture.reconstituteWithId(existingId);

            // Act
            final var entity = mapper.toUpdateEntity(product);

            // Assert
            assertThat(entity)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("isNew") // isNew does not exist in the Domain, so we ignore it.
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(product);

            assertThat(entity.isNew()).isFalse();
        }

        @Test
        @DisplayName("Should return null when source product is null")
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
        @DisplayName("Should reconstitute ProductEntity into domain Product aggregate")
        void shouldMapToDomain() {
            // Arrange
            final var expectedId = UUID.randomUUID();
            final var entity = ProductEntityFixture.createWithId(expectedId);

            // Act
            final var product = mapper.toDomain(entity);

            // Assert
            assertThat(product)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(entity);
        }

        @Test
        @DisplayName("Should return null when source entity is null")
        void shouldReturnNullWhenEntityIsNull() {
            // Act
            final var product = mapper.toDomain(null);

            // Assert
            assertThat(product).isNull();
        }
    }
}
