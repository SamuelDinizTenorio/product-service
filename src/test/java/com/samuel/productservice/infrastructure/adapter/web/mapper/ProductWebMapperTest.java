package com.samuel.productservice.infrastructure.adapter.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.infrastructure.adapter.web.fixture.ProductRequestFixture;

@DisplayName("ProductWebMapper Unit Tests")
class ProductWebMapperTest {

    private final ProductWebMapper mapper = new ProductWebMapper();

    @Nested
    @DisplayName("toDomain Mapping Tests")
    class ToDomain {
        
        @Test
        @DisplayName("Should map ProductRequest into domain Product aggregate")
        void shouldMapProductRequestToDomainProductAggregate() {
            // Arrage
            final var request = ProductRequestFixture.any();

            // Act
            final var product = mapper.toDomain(request);

            // Assert
            assertThat(product)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("sku", "id")
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(request);

            assertThat(product.getSku().getValue()).isEqualTo(request.sku());
        }

        @Test
        @DisplayName("Should throw NullPointerException when productRequest is null")
        void shouldThrowExceptionWhenRequestIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> mapper.toDomain(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("productRequest cannot be null");
        }
    }

    @Nested
    @DisplayName("toResponse Mapping Tests")
    class ToResponse {

        @Test
        @DisplayName("Should map domain Product into ProductResponse data transfer object")
        void shouldMapProductToProductResponse() {
            // Arrange
            final var product = ProductFixture.any();

            // Act
            final var response = mapper.toResponse(product);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("sku") // String vs Sku Object
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .isEqualTo(product);

            assertThat(response.sku()).isEqualTo(product.getSku().getValue());
            assertThat(response.id()).isEqualTo(product.getId());
        }

        @Test
        @DisplayName("Should throw NullPointerException when product domain model is null")
        void shouldThrowExceptionWhenProductIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> mapper.toResponse(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("product cannot be null");
        }
    }
}
