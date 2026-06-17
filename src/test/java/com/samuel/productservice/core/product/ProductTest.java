package com.samuel.productservice.core.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.samuel.productservice.core.exception.NotificationException;

@DisplayName("Product Domain Entity Tests")
public class ProductTest {

    protected static Stream<Arguments> getPossiblesOfValueProduct() {
        final var bigText = "a".repeat(201);
        final var ten = BigDecimal.TEN;
        final var twenty = BigDecimal.valueOf(20);
        final var thirty = BigDecimal.valueOf(30);

        return Stream.of(
                Arguments.of("", "Product name", ten, twenty, thirty, "sku cannot be null or blank"),
                Arguments.of(null, "Product name", ten, twenty, thirty, "sku cannot be null or blank"),
                Arguments.of("1", null, ten, twenty, thirty, "name cannot be null"),
                Arguments.of("1", "Product name", null, twenty, thirty, "stock cannot be null"),
                Arguments.of("1", "Product name", ten, null, thirty, "cost cannot be null"),
                Arguments.of("1", "Product name", ten, twenty, null, "price cannot be null"),
                Arguments.of("1", "Prod   ", ten, twenty, thirty, "name must be between 5 and 200 characters"),
                Arguments.of("1", bigText, ten, twenty, thirty, "name must be between 5 and 200 characters"),
                Arguments.of("1", "Product name", BigDecimal.valueOf(-1), twenty, thirty, "stock must be positive"),
                Arguments.of("1", "Product name", ten, BigDecimal.ZERO, thirty, "cost must be greater than zero"),
                Arguments.of("1", "Product name", ten, twenty, twenty, "price must be greater than cost"));
    }

    @Nested
    @DisplayName("Product Creation Tests")
    class Create {

        @Test
        @DisplayName("Should create a valid product")
        void shouldInstanceNewProduct() {
            final var expectedSku = "1";
            final var expectedName = "Product name";
            final var expectedStock = BigDecimal.valueOf(10);
            final var expectedCost = BigDecimal.valueOf(20);
            final var expectedPrice = BigDecimal.valueOf(30);

            final var product = Product.create(expectedSku, expectedName, expectedStock, expectedCost, expectedPrice);

            assertThat(product.getId()).isNotNull();
            assertThat(product.getSku()).isEqualTo(expectedSku);
            assertThat(product.getName()).isEqualTo(expectedName);
            assertThat(product.getStock()).isEqualTo(expectedStock);
            assertThat(product.getCost()).isEqualTo(expectedCost);
            assertThat(product.getPrice()).isEqualTo(expectedPrice);
        }

        @ParameterizedTest
        @MethodSource("getPossiblesOfValueProduct")
        @DisplayName("Should not create product with invalid data")
        void shouldNotInstanceNewProduct(String sku, String name, BigDecimal stock, BigDecimal cost, BigDecimal price,
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
    @DisplayName("Product Update Tests")
    class Update {

        @Test
        @DisplayName("Should update a product with valid data")
        void shouldInstanceNewProductAndUpdate() {
            final var expectedSku = "1";
            final var expectedName = "Product name";
            final var expectedStock = BigDecimal.valueOf(10);
            final var expectedCost = BigDecimal.valueOf(20);
            final var expectedPrice = BigDecimal.valueOf(30);

            final var product = Product.create("2", "original name", BigDecimal.valueOf(100), BigDecimal.valueOf(200),
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
        @MethodSource("getPossiblesOfValueProduct")
        @DisplayName("Should not update product with invalid data")
        void shouldInstanceNewProductAndNotUpdate(String sku, String name, BigDecimal stock, BigDecimal cost,
                BigDecimal price, String expectedMessage) {
            final var product = Product.create("2", "original name", BigDecimal.valueOf(100), BigDecimal.valueOf(200),
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
            final var product = Product.create("2", "original name", BigDecimal.valueOf(100), BigDecimal.valueOf(200),
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
