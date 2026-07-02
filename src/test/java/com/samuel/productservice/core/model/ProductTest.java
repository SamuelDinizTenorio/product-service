package com.samuel.productservice.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

@DisplayName("Product Domain Entity Tests")
class ProductTest {

        private static Stream<Arguments> getPossiblesOfValueProduct() {
                final var bigText = "a".repeat(201);
                final var ten = BigDecimal.TEN;
                final var twenty = BigDecimal.valueOf(20);
                final var thirty = BigDecimal.valueOf(30);

                return Stream.of(
                                Arguments.of("", "Mouse", ten, twenty, thirty,
                                                "sku cannot be null or blank"),
                                Arguments.of(null, "Mouse", ten, twenty, thirty,
                                                "sku cannot be null or blank"),
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
                                                "stock must be positive"),
                                Arguments.of("SKU-123", "Mouse", ten, BigDecimal.ZERO, thirty,
                                                "cost must be greater than zero"),
                                Arguments.of("SKU-123", "Mouse", ten, twenty, twenty,
                                                "price must be greater than cost"));
        }

        @Nested
        @DisplayName("Product Creation Tests")
        class Create {

                @Test
                @DisplayName("Should create a valid product")
                void shouldInstanceNewProduct() {
                        final var expectedSku = "SKU-123";
                        final var expectedName = "Mouse";
                        final var expectedStock = BigDecimal.valueOf(10);
                        final var expectedCost = BigDecimal.valueOf(20);
                        final var expectedPrice = BigDecimal.valueOf(30);

                        final var product = Product.create(expectedSku, expectedName, expectedStock, expectedCost,
                                        expectedPrice);

                        assertThat(product.getId())
                                        .isNotNull()
                                        .isInstanceOf(UUID.class);
                        assertThat(product.getSku()).isEqualTo(expectedSku);
                        assertThat(product.getName()).isEqualTo(expectedName);
                        assertThat(product.getStock()).isEqualTo(expectedStock);
                        assertThat(product.getCost()).isEqualTo(expectedCost);
                        assertThat(product.getPrice()).isEqualTo(expectedPrice);
                }

                @ParameterizedTest
                @MethodSource("com.samuel.productservice.core.model.ProductTest#getPossiblesOfValueProduct")
                @DisplayName("Should not create product with invalid data")
                void shouldNotInstanceNewProduct(String sku, String name, BigDecimal stock, BigDecimal cost,
                                BigDecimal price,
                                String expectedMessage) {
                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                Product.create(sku, name, stock, cost, price);
                        });
                        assertThat(expectedError.getErrors().get(0).message()).isEqualTo(expectedMessage);
                }

                @Test
                @DisplayName("Should collect multiple errors on creation")
                void shouldCollectMultipleErrorsWhenInstancingNewProduct() {
                        final String invalidSku = "";
                        final String invalidName = null;
                        final BigDecimal invalidStock = BigDecimal.valueOf(-1);
                        final BigDecimal validCost = BigDecimal.valueOf(10);
                        final BigDecimal invalidPrice = BigDecimal.valueOf(5);

                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                Product.create(invalidSku, invalidName, invalidStock, validCost, invalidPrice);
                        });

                        assertThat(expectedError.getErrors()).hasSize(4);
                        assertThat(expectedError.getErrors())
                                        .extracting(error -> error.message())
                                        .containsExactlyInAnyOrder(
                                                        "sku cannot be null or blank",
                                                        "name cannot be null",
                                                        "stock must be positive",
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
                        final var expectedSku = "SKU-123";
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
                        assertThat(product.getStock()).isEqualTo(expectedStock);
                        assertThat(product.getCost()).isEqualTo(expectedCost);
                        assertThat(product.getPrice()).isEqualTo(expectedPrice);
                }

                @Test
                @DisplayName("Should throw NullPointerException when ID is null during reconstitution")
                void shouldThrowExceptionWhenIdIsNull() {
                        // Arrange
                        final UUID nullId = null;
                        final var validSku = "SKU-123";
                        final var validName = "Mouse";
                        final var validStock = BigDecimal.TEN;
                        final var validCost = BigDecimal.TEN;
                        final var validPrice = BigDecimal.valueOf(20);

                        // Act & Assert
                        final var exception = assertThrows(NullPointerException.class, () -> {
                                Product.reconstitute(nullId, validSku, validName, validStock, validCost, validPrice);
                        });

                        // Garante que a mensagem de erro explicativa que colocou no
                        // Objects.requireNonNull foi disparada
                        assertThat(exception.getMessage())
                                        .contains("Product identity (ID) cannot be null during reconstitution");
                }

                @ParameterizedTest
                @MethodSource("com.samuel.productservice.core.model.ProductTest#getPossiblesOfValueProduct")
                @DisplayName("Should not reconstitute product if persistent data violates domain rules")
                void shouldNotReconstituteProductWithInvalidData(
                                String sku, String name, BigDecimal stock, BigDecimal cost, BigDecimal price,
                                String expectedMessage) {

                        // Arrange
                        final var existingId = UUID.randomUUID();

                        // Act & Assert
                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                Product.reconstitute(existingId, sku, name, stock, cost, price);
                        });

                        assertThat(expectedError.getErrors().get(0).message()).isEqualTo(expectedMessage);
                }

                @Test
                @DisplayName("Should collect multiple errors on reconstitution if data is severely corrupted")
                void shouldCollectMultipleErrorsOnReconstitution() {
                        // Arrange
                        final var existingId = UUID.randomUUID();
                        final String invalidSku = "";
                        final String invalidName = null;
                        final BigDecimal invalidStock = BigDecimal.valueOf(-5);
                        final BigDecimal validCost = BigDecimal.valueOf(10);
                        final BigDecimal invalidPrice = BigDecimal.valueOf(2);

                        // Act & Assert
                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                Product.reconstitute(existingId, invalidSku, invalidName, invalidStock, validCost,
                                                invalidPrice);
                        });

                        assertThat(expectedError.getErrors()).hasSize(4);
                        assertThat(expectedError.getErrors()
                                        .stream()
                                        .map(error -> error.message())
                                        .toList())
                                        .containsExactlyInAnyOrder(
                                                        "sku cannot be null or blank",
                                                        "name cannot be null",
                                                        "stock must be positive",
                                                        "price must be greater than cost");
                }
        }

        @Nested
        @DisplayName("Product Update Tests")
        class Update {

                @Test
                @DisplayName("Should update a product with valid data")
                void shouldInstanceNewProductAndUpdate() {
                        final var expectedSku = "SKU-123";
                        final var expectedName = "Mouse";
                        final var expectedStock = BigDecimal.valueOf(10);
                        final var expectedCost = BigDecimal.valueOf(20);
                        final var expectedPrice = BigDecimal.valueOf(30);

                        final var product = Product.create("2", "original name", BigDecimal.valueOf(100),
                                        BigDecimal.valueOf(200),
                                        BigDecimal.valueOf(300));
                        final var expectedId = product.getId();

                        product.update(expectedSku, expectedName, expectedStock, expectedCost, expectedPrice);

                        assertThat(product.getId()).isEqualTo(expectedId);
                        assertThat(product.getSku()).isEqualTo(expectedSku);
                        assertThat(product.getName()).isEqualTo(expectedName);
                        assertThat(product.getStock()).isEqualTo(expectedStock);
                        assertThat(product.getCost()).isEqualTo(expectedCost);
                        assertThat(product.getPrice()).isEqualTo(expectedPrice);
                }

                @ParameterizedTest
                @MethodSource("com.samuel.productservice.core.model.ProductTest#getPossiblesOfValueProduct")
                @DisplayName("Should not update product with invalid data")
                void shouldInstanceNewProductAndNotUpdate(String sku, String name, BigDecimal stock, BigDecimal cost,
                                BigDecimal price, String expectedMessage) {
                        final var product = Product.create("2", "original name", BigDecimal.valueOf(100),
                                        BigDecimal.valueOf(200),
                                        BigDecimal.valueOf(300));

                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                product.update(sku, name, stock, cost, price);
                        });

                        assertThat(expectedError.getErrors().get(0).message()).isEqualTo(expectedMessage);
                }

                @Test
                @DisplayName("Should collect multiple errors on update")
                void shouldCollectMultipleErrorsOnUpdate() {
                        // Arrange
                        final var product = Product.create("2", "original name", BigDecimal.valueOf(100),
                                        BigDecimal.valueOf(200),
                                        BigDecimal.valueOf(300));

                        final String invalidSku = "";
                        final String invalidName = null;
                        final BigDecimal invalidStock = BigDecimal.valueOf(-1);
                        final BigDecimal validCost = BigDecimal.valueOf(10);
                        final BigDecimal invalidPrice = BigDecimal.valueOf(5);

                        // Act
                        final var expectedError = assertThrows(NotificationException.class, () -> {
                                product.update(invalidSku, invalidName, invalidStock, validCost, invalidPrice);
                        });

                        // Assert
                        assertThat(expectedError.getErrors()).hasSize(4);
                        assertThat(expectedError.getErrors())
                                        .extracting(error -> error.message())
                                        .containsExactlyInAnyOrder(
                                                        "sku cannot be null or blank",
                                                        "name cannot be null",
                                                        "stock must be positive",
                                                        "price must be greater than cost");
                }
        }
}
