package com.samuel.productservice.core.usecase;

import java.math.BigDecimal;
import java.util.Optional;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.gateway.ProductGateway;
import com.samuel.productservice.core.model.Product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetProductBySkuUseCase Unit Tests")
class GetProductBySkuUseCaseTest {

        @Mock
        private ProductGateway repository;

        @InjectMocks
        private GetProductBySkuUseCase getProductBySkuUseCase;

        @Nested
        @DisplayName("Execute Method Scenarios")
        class ExecuteMethod {

                @Test
                @DisplayName("Should successfully return the product when it exists in the repository")
                void shouldReturnProductWhenProductExists() {
                        // Arrange
                        final var expectedProduct = ProductFixture.any();
                        final var productSku = expectedProduct.getSku();

                        given(repository.findBySku(productSku))
                                        .willReturn(Optional.of(expectedProduct));

                        // Act
                        Product actualProduct = getProductBySkuUseCase.execute(productSku);

                        // Assert
                        assertThat(actualProduct)
                                        .isNotNull()
                                        .isSameAs(expectedProduct);
                        verify(repository).findBySku(productSku);
                }

                @Test
                @DisplayName("Should throw NotFoundException when the product does not exist in the repository")
                void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
                        // Arrange
                        final var productSku = "SKU-NON-EXISTENT";
                        given(repository.findBySku(productSku))
                                        .willReturn(Optional.empty());

                        // Act & Assert
                        assertThatThrownBy(() -> getProductBySkuUseCase.execute(productSku))
                                        .isInstanceOf(NotFoundException.class)
                                        .hasMessageContaining(productSku);
                        verify(repository).findBySku(productSku);
                }
        }
}
