package com.samuel.productservice.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.samuel.productservice.core.validation.NotificationError;
import com.samuel.productservice.core.validation.NotificationValidation;

@DisplayName("Sku Value Object Unit Tests")
class SkuTest {

    private static Stream<Arguments> getInvalidSkusForValidation() {
        return Stream.of(
                // Presence & Blank validations
                Arguments.of("", "sku cannot be null or blank"),
                Arguments.of(null, "sku cannot be null or blank"),
                Arguments.of("   ", "sku cannot be null or blank"),

                // Length bounds
                Arguments.of("SK", "sku must be between 3 and 30 characters"),
                Arguments.of("A".repeat(31), "sku must be between 3 and 30 characters"),

                // Pattern restrictions
                Arguments.of("SKU@123", "sku must contain only alphanumeric characters, hyphens, or underscores"),
                Arguments.of("SKU 123", "sku must contain only alphanumeric characters, hyphens, or underscores"),
                Arguments.of("SKU.123", "sku must contain only alphanumeric characters, hyphens, or underscores"));
    }

    @Nested
    @DisplayName("Sku Equality Tests (Structural Equality)")
    class EqualityAndHashCode {

        @Test
        @DisplayName("Should be equal when internal values match exactly")
        void shouldBeEqualWhenValuesAreIdentical() {
            // Arrange
            final var skuA = Sku.reconstitute("SKU-123");
            final var skuB = Sku.reconstitute("SKU-123");

            // Act & Assert
            assertThat(skuA).isEqualTo(skuB);
            assertThat(skuA.hashCode()).isEqualTo(skuB.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when internal values differ")
        void shouldNotBeEqualWhenValuesDiffer() {
            // Arrange
            final var skuA = Sku.reconstitute("SKU-123");
            final var skuB = Sku.reconstitute("SKU-999");

            // Act & Assert
            assertThat(skuA).isNotEqualTo(skuB);
            assertThat(skuA.hashCode()).isNotEqualTo(skuB.hashCode());
        }
    }

    @Nested
    @DisplayName("Sku Creation and Validation Tests (of)")
    class CreationAndValidation {

        @Test
        @DisplayName("Should create a valid Sku converting input to uppercase and trimming whitespace")
        void shouldCreateValidSkuWithNormalization() {
            // Arrange
            final var rawInput = "   sku-abc-123   ";
            final var expectedValue = "SKU-ABC-123";
            final var notification = NotificationValidation.create();

            // Act
            final var sku = Sku.of(rawInput, notification);

            // Assert
            assertThat(sku).isNotNull();
            assertThat(sku.getValue()).isEqualTo(expectedValue);
            assertThat(notification.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("Should create valid Sku at the minimum length boundary (3 characters)")
        void shouldCreateSkuAtMinimumLengthBoundary() {
            final var notification = NotificationValidation.create();
            final var sku = Sku.of("ABC", notification);

            assertThat(sku).isNotNull();
            assertThat(sku.getValue()).isEqualTo("ABC");
            assertThat(notification.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("Should create valid Sku at the maximum length boundary (30 characters)")
        void shouldCreateSkuAtMaximumLengthBoundary() {
            final var input = "A".repeat(30);
            final var notification = NotificationValidation.create();
            final var sku = Sku.of(input, notification);

            assertThat(sku).isNotNull();
            assertThat(sku.getValue()).isEqualTo(input);
            assertThat(notification.hasErrors()).isFalse();
        }

        @ParameterizedTest
        @MethodSource("com.samuel.productservice.core.model.SkuTest#getInvalidSkusForValidation")
        @DisplayName("Should return null and accumulate specific error message into notification when input is invalid")
        void shouldFailValidationAndAccumulateError(String rawValue, String expectedMessage) {
            // Arrange
            final var notification = NotificationValidation.create();

            // Act
            final var sku = Sku.of(rawValue, notification);

            // Assert
            assertThat(sku).isNull();
            assertThat(notification.hasErrors()).isTrue();
            assertThat(notification.getErrors())
                    .extracting(NotificationError::message)
                    .contains(expectedMessage);
        }

        @Test
        @DisplayName("Should accumulate multiple distinct errors when Sku breaks multiple validation invariants")
        void shouldAccumulateMultipleErrors() {
            // Arrange
            final var invalidInput = "a@"; // Length is 2 (< 3) and contains illegal character '@'
            final var notification = NotificationValidation.create();

            // Act
            final var sku = Sku.of(invalidInput, notification);

            // Assert
            assertThat(sku).isNull();
            assertThat(notification.hasErrors()).isTrue();
            assertThat(notification.getErrors())
                    .extracting(NotificationError::message)
                    .containsExactlyInAnyOrder(
                            "sku must be between 3 and 30 characters",
                            "sku must contain only alphanumeric characters, hyphens, or underscores");
        }
    }

    @Nested
    @DisplayName("Sku Reconstitution Tests")
    class Reconstitution {

        @Test
        @DisplayName("Should bypass all domain validation rules entirely when using reconstitute factory")
        void shouldReconstituteBypassingValidation() {
            // Arrange
            final var rawValue = "invalid string completely broken!!";

            // Act
            final var sku = Sku.reconstitute(rawValue);

            // Assert
            assertThat(sku).isNotNull();
            assertThat(sku.getValue()).isEqualTo(rawValue); // Retains original input without cleanups or exceptions
        }
    }

    @Nested
    @DisplayName("Sku String Representation Tests")
    class ToStringRepresentation {

        @Test
        @DisplayName("Should return the encapsulated raw string value when calling toString")
        void shouldReturnInternalValueOnToString() {
            // Arrange
            final var expectedValue = "SKU-FINAL-999";
            final var sku = Sku.reconstitute(expectedValue);

            // Act & Assert
            assertThat(sku.toString()).isEqualTo(expectedValue);
        }
    }
}
