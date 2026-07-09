package com.samuel.productservice.infrastructure.adapter.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.samuel.productservice.infrastructure.adapter.persistence.fixture.ProductEntityFixture;

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
                    "Keyboard",
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    true);

            final var entityB = ProductEntityFixture.createWithIdAndIsNew(commonId, false);

            // Act & Assert
            assertThat(entityA).isEqualTo(entityB);
            assertThat(entityA.hashCode()).isEqualTo(entityB.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when entities have different IDs, even with identical attributes")
        void shouldNotBeEqualWhenDifferentId() {
            // Arrange
            final var entityA = ProductEntityFixture.any();
            final var entityB = ProductEntityFixture.any();

            // Act & Assert
            assertThat(entityA).isNotEqualTo(entityB);
            assertThat(entityA.hashCode()).isNotEqualTo(entityB.hashCode());
        }

        @Test
        @DisplayName("Should handle comparison with null and different object types safely")
        void shouldHandleNullAndDifferentTypes() {
            // Arrange
            final var entity = ProductEntityFixture.any();

            // Act & Assert
            assertThat(entity).isNotEqualTo(null);
            assertThat(entity).isNotEqualTo("a string object");
        }
    }
}
