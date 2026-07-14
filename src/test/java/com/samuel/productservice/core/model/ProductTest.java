package com.samuel.productservice.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.throwable;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.samuel.productservice.core.exception.NotificationException;
import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.validation.NotificationError;

@DisplayName("Product Domain Entity Tests")
class ProductTest {

        private static Stream<Arguments> getPossiblesOfValueProduct() {
                final var bigText = "a".repeat(201);
                final var ten = BigDecimal.TEN;
                final var twenty = BigDecimal.valueOf(20);
                final var thirty = BigDecimal.valueOf(30);

                return Stream.of(
                                // SKU Presence & Blank validations
                                Arguments.of("", "Mouse", ten, twenty, thirty,
                                                "sku cannot be null or blank"),
                                Arguments.of(null, "Mouse", ten, twenty, thirty,
                                                "sku cannot be null or blank"),

                                // NEW: SKU Length & Format validations
                                Arguments.of("SK", "Mouse", ten, twenty, thirty,
                                                "sku must be between 3 and 30 characters"),
                                Arguments.of("A".repeat(31), "Mouse", ten, twenty, thirty,
                                                "sku must be between 3 and 30 characters"),
                                Arguments.of("SKU@123", "Mouse", ten, twenty, thirty,
                                                "sku must contain only alphanumeric characters, hyphens, or underscores"),

                                // Name, Stock, Cost, Price validations
                                Arguments.of("SKU-123", null, ten, twenty, thirty,
                                                "name cannot be null"),
                                Arguments.of("SKU-123", "Mouse", null, twenty, thirty,
                                                "stock cannot be null"),
                                Arguments.of("SKU-123", "Mouse", ten, null, thirty,
                                                "cost cannot be null"),
                                Arguments.of("SKU-123", "Mouse", ten, twenty, null,
                                                "price cannot be null"),
                                Arguments.of("SKU-123", "Mous   ", ten, twenty, thirty,
                                                "name must be between 5 and 200 characters"),
                                Arguments.of("SKU-123", bigText, ten, twenty, thirty,
                                                "name must be between 5 and 200 characters"),
                                Arguments.of("SKU-123", "Mouse", BigDecimal.valueOf(-1), twenty, thirty,
                                                "stock cannot be negative"),

                                Arguments.of("SKU-123", "Mouse", ten, BigDecimal.ZERO, thirty,
                                                "cost must be greater than zero"),
                                Arguments.of("SKU-123", "Mouse", ten, twenty, twenty,
                                                "price must be greater than cost"));
        }

        @Nested
        @DisplayName("Product Equality Tests (Equals and HashCode)")
        class EqualsAndHashCodeTest {

                @Test
                @DisplayName("Should be equal when products have the same ID, even with different attributes")
                void shouldBeEqualWhenSameId() {
                        // Arrange
                        final var id = UUID.randomUUID();
                        final var productA = Product.reconstitute(
                                        id,
                                        Sku.reconstitute("SKU-1"),
                                        "Mouse",
                                        BigDecimal.TEN,
                                        BigDecimal.TEN,
                                        BigDecimal.valueOf(20));
                        final var productB = ProductFixture.reconstituteWithId(id);

                        // Act & Assert
                        assertThat(productA).isEqualTo(productB);
                        assertThat(productA.hashCode()).isEqualTo(productB.hashCode());
                }

                @Test
                @DisplayName("Should not be equal when products have different IDs, even with identical attributes")
                void shouldNotBeEqualWhenDifferentId() {
                        // Arrange
                        final var productA = ProductFixture.any();
                        final var productB = ProductFixture.any();

                        // Act & Assert
                        assertThat(productA).isNotEqualTo(productB);
                        assertThat(productA.hashCode()).isNotEqualTo(productB.hashCode());
                }
        }

        @Nested
        @DisplayName("Product Creation Tests")
        class Create {

                @Test
                @DisplayName("Should create a valid product")
                void shouldInstanceNewProduct() {
                        // Arrange
                        final var expectedSku = "SKU-123";
                        final var expectedName = "Mouse";
                        final var expectedStock = BigDecimal.valueOf(10);
                        final var expectedCost = BigDecimal.valueOf(20);
                        final var expectedPrice = BigDecimal.valueOf(30);

                        // Act
                        final var product = Product.create(
                                        expectedSku,
                                        expectedName,
                                        expectedStock,
                                        expectedCost,
                                        expectedPrice);

                        // Assert
                        assertThat(product.getId())
                                        .isNotNull()
                                        .isInstanceOf(UUID.class);
                        assertThat(product.getSku()).isEqualTo(Sku.reconstitute(expectedSku));
                        assertThat(product.getName()).isEqualTo(expectedName);
                        assertThat(product.getStock()).isEqualByComparingTo(expectedStock);
                        assertThat(product.getCost()).isEqualByComparingTo(expectedCost);
                        assertThat(product.getPrice()).isEqualByComparingTo(expectedPrice);
                }

                @ParameterizedTest
                @MethodSource("com.samuel.productservice.core.model.ProductTest#getPossiblesOfValueProduct")
                @DisplayName("Should not create product with invalid data")
                void shouldNotInstanceNewProduct(String sku, String name, BigDecimal stock, BigDecimal cost,
                                BigDecimal price, String expectedMessage) {
                        // Act & Assert Fluido
                        assertThatThrownBy(() -> Product.create(sku, name, stock, cost, price))
                                        .isInstanceOf(NotificationException.class)
                                        .asInstanceOf(throwable(NotificationException.class))
                                        .extracting(NotificationException::getErrors)
                                        .asInstanceOf(list(NotificationError.class))
                                        .extracting(NotificationError::message)
                                        .contains(expectedMessage);
                }

                @Test
                @DisplayName("Should collect multiple errors on creation")
                void shouldCollectMultipleErrorsWhenInstancingNewProduct() {
                        // Arrange
                        final String invalidSku = "";
                        final String invalidName = null;
                        final BigDecimal invalidStock = BigDecimal.valueOf(-1);
                        final BigDecimal validCost = BigDecimal.valueOf(10);
                        final BigDecimal invalidPrice = BigDecimal.valueOf(5);

                        // Act & Assert Fluido
                        assertThatThrownBy(() -> Product.create(invalidSku, invalidName, invalidStock, validCost,
                                        invalidPrice))
                                        .isInstanceOf(NotificationException.class)
                                        .asInstanceOf(throwable(NotificationException.class))
                                        .extracting(NotificationException::getErrors)
                                        .asInstanceOf(list(NotificationError.class))
                                        .hasSize(4)
                                        .extracting(NotificationError::message)
                                        .containsExactlyInAnyOrder(
                                                        "sku cannot be null or blank",
                                                        "name cannot be null",
                                                        "stock cannot be negative",
                                                        "price must be greater than cost");
                }
        }

        @Nested
        @DisplayName("Product Reconstitution Tests")
        class Reconstitute {

                @Test
                @DisplayName("Should reconstitute a product with an existing UUID and valid data")
                void shouldReconstituteProduct() {
                        // Arrange
                        final var expectedId = UUID.randomUUID();
                        final var expectedSkuValue = "SKU-123";
                        final var expectedSku = Sku.reconstitute(expectedSkuValue);
                        final var expectedName = "Mouse";
                        final var expectedStock = BigDecimal.valueOf(50);
                        final var expectedCost = BigDecimal.valueOf(10.50);
                        final var expectedPrice = BigDecimal.valueOf(25.99);

                        // Act
                        final var product = Product.reconstitute(
                                        expectedId,
                                        expectedSku,
                                        expectedName,
                                        expectedStock,
                                        expectedCost,
                                        expectedPrice);

                        // Assert
                        assertThat(product).isNotNull();
                        assertThat(product.getId()).isEqualTo(expectedId);
                        assertThat(product.getSku()).isEqualTo(expectedSku);
                        assertThat(product.getName()).isEqualTo(expectedName);
                        assertThat(product.getStock()).isEqualByComparingTo(expectedStock);
                        assertThat(product.getCost()).isEqualByComparingTo(expectedCost);
                        assertThat(product.getPrice()).isEqualByComparingTo(expectedPrice);
                }

                @Test
                @DisplayName("Should throw NullPointerException when ID is null during reconstitution")
                void shouldThrowExceptionWhenIdIsNull() {
                        // Arrange
                        final UUID nullId = null;
                        final var validSku = Sku.reconstitute("SKU-123");
                        final var validName = "Mouse";
                        final var validStock = BigDecimal.TEN;
                        final var validCost = BigDecimal.TEN;
                        final var validPrice = BigDecimal.valueOf(20);

                        // Act & Assert
                        assertThatThrownBy(() -> Product.reconstitute(
                                        nullId, validSku, validName, validStock, validCost, validPrice))
                                        .isInstanceOf(NullPointerException.class)
                                        .hasMessageContaining(
                                                        "Product identity (ID) cannot be null during reconstitution");
                }
        }

        @Nested
        @DisplayName("Product Update Tests")
        class Update {

                @Test
                @DisplayName("Should update a product with valid data")
                void shouldInstanceNewProductAndUpdate() {
                        // Arrange
                        final var expectedSku = "SKU-123";
                        final var expectedName = "Mouse";
                        final var expectedStock = BigDecimal.valueOf(10);
                        final var expectedCost = BigDecimal.valueOf(20);
                        final var expectedPrice = BigDecimal.valueOf(30);

                        final var product = ProductFixture.any();
                        final var expectedId = product.getId();

                        // Act
                        product.update(
                                        expectedSku,
                                        expectedName,
                                        expectedStock,
                                        expectedCost,
                                        expectedPrice);

                        // Assert
                        assertThat(product.getId()).isEqualTo(expectedId);
                        assertThat(product.getSku()).isEqualTo(Sku.reconstitute(expectedSku));
                        assertThat(product.getName()).isEqualTo(expectedName);
                        assertThat(product.getStock()).isEqualByComparingTo(expectedStock);
                        assertThat(product.getCost()).isEqualByComparingTo(expectedCost);
                        assertThat(product.getPrice()).isEqualByComparingTo(expectedPrice);
                }

                @ParameterizedTest
                @MethodSource("com.samuel.productservice.core.model.ProductTest#getPossiblesOfValueProduct")
                @DisplayName("Should not update product with invalid data")
                void shouldInstanceNewProductAndNotUpdate(String sku, String name, BigDecimal stock, BigDecimal cost,
                                BigDecimal price, String expectedMessage) {
                        // Arrange
                        final var product = ProductFixture.any();

                        // Act & Assert
                        assertThatThrownBy(() -> product.update(sku, name, stock, cost, price))
                                        .isInstanceOf(NotificationException.class)
                                        .asInstanceOf(throwable(NotificationException.class))
                                        .extracting(NotificationException::getErrors)
                                        .asInstanceOf(list(NotificationError.class))
                                        .extracting(NotificationError::message)
                                        .contains(expectedMessage);
                }

                @Test
                @DisplayName("Should collect multiple errors on update")
                void shouldCollectMultipleErrorsOnUpdate() {
                        // Arrange
                        final var product = ProductFixture.any();

                        final String invalidSku = "";
                        final String invalidName = null;
                        final BigDecimal invalidStock = BigDecimal.valueOf(-1);
                        final BigDecimal validCost = BigDecimal.valueOf(10);
                        final BigDecimal invalidPrice = BigDecimal.valueOf(5);

                        // Act & Assert
                        assertThatThrownBy(() -> product.update(invalidSku, invalidName, invalidStock, validCost,
                                        invalidPrice))
                                        .isInstanceOf(NotificationException.class)
                                        .asInstanceOf(throwable(NotificationException.class))
                                        .extracting(NotificationException::getErrors)
                                        .asInstanceOf(list(NotificationError.class))
                                        .hasSize(4)
                                        .extracting(NotificationError::message)
                                        .containsExactlyInAnyOrder(
                                                        "sku cannot be null or blank",
                                                        "name cannot be null",
                                                        "stock cannot be negative",
                                                        "price must be greater than cost");
                }
        }
}
