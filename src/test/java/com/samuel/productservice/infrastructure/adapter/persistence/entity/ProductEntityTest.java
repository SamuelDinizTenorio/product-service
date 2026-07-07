package com.samuel.productservice.infrastructure.adapter.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductEntity Unit Tests")
class ProductEntityTest {

    @Nested
    @DisplayName("Equality and HashCode Contract Tests")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("Should be equal when entities have the same ID, even with different attributes")
        void shouldBeEqualWhenSameId() {
            // Arrange
            final var commonId = UUID.randomUUID();

            final var entityA = ProductEntity.create(
                    commonId,
                    "SKU-1",
                    "Original Keyboard",
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    true);

            final var entityB = ProductEntity.create(
                    commonId,
                    "SKU-2",
                    "Modified Keyboard",
                    BigDecimal.ZERO,
                    BigDecimal.ONE,
                    BigDecimal.ONE,
                    false);

            // Act & Assert
            assertThat(entityA).isEqualTo(entityB);
            assertThat(entityA.hashCode()).isEqualTo(entityB.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when entities have different IDs, even with identical attributes")
        void shouldNotBeEqualWhenDifferentId() {
            // Arrange
            final var sku = "SKU-123";
            final var name = "Keyboard";
            final var stock = BigDecimal.TEN;
            final var cost = BigDecimal.valueOf(150.00);
            final var price = BigDecimal.valueOf(299.90);

            final var entityA = ProductEntity.create(UUID.randomUUID(), sku, name, stock, cost, price, true);
            final var entityB = ProductEntity.create(UUID.randomUUID(), sku, name, stock, cost, price, true);

            // Act & Assert
            assertThat(entityA).isNotEqualTo(entityB);
            assertThat(entityA.hashCode()).isNotEqualTo(entityB.hashCode());
        }

        @Test
        @DisplayName("Should handle comparison with null and different object types safely")
        void shouldHandleNullAndDifferentTypes() {
            // Arrange
            final var entity = ProductEntity.create(
                    UUID.randomUUID(),
                    "SKU-123",
                    "Keyboard",
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    true);

            // Act & Assert
            assertThat(entity).isNotEqualTo(null);
            assertThat(entity).isNotEqualTo("a string object");
        }
    }
}
