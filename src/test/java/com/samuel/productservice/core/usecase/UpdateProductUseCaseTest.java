package com.samuel.productservice.core.usecase;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.gateway.ProductGateway;
import com.samuel.productservice.core.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductUseCase Unit Tests")
class UpdateProductUseCaseTest {

        @Mock
        private ProductGateway repository;

        @Mock
        private GetProductByIdUseCase getProductByIdUseCase;

        @InjectMocks
        private UpdateProductUseCase updateProductUseCase;

        @Nested
        @DisplayName("Execute Method Scenarios")
        class ExecuteMethod {

                @Test
                @DisplayName("Should successfully update and return an existing product")
                void shouldUpdateAndReturnExistingProduct() {
                        // Arrange
                        final var existingProduct = ProductFixture.any();
                        final var productId = existingProduct.getId();

                        final var updatedProductData = Product.create(
                                        "SKU-456",
                                        "Keyboard",
                                        BigDecimal.valueOf(20),
                                        BigDecimal.valueOf(50.00),
                                        BigDecimal.valueOf(99.90));

                        given(getProductByIdUseCase.execute(productId))
                                        .willReturn(existingProduct);
                        given(repository.update(existingProduct))
                                        .willReturn(existingProduct);

                        // Act
                        Product actualProduct = updateProductUseCase.execute(productId, updatedProductData);

                        // Assert
                        assertThat(actualProduct)
                                        .isNotNull()
                                        .isSameAs(existingProduct);
                        assertThat(actualProduct.getSku()).isEqualTo(updatedProductData.getSku());
                        assertThat(actualProduct.getName()).isEqualTo(updatedProductData.getName());
                        assertThat(actualProduct.getStock()).isEqualByComparingTo(updatedProductData.getStock());
                        assertThat(actualProduct.getCost()).isEqualByComparingTo(updatedProductData.getCost());
                        assertThat(actualProduct.getPrice()).isEqualByComparingTo(updatedProductData.getPrice());

                        verify(getProductByIdUseCase).execute(productId);
                        verify(repository).update(existingProduct);
                }

        }
}
