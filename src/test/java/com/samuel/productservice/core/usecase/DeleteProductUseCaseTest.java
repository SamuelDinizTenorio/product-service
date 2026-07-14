package com.samuel.productservice.core.usecase;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.gateway.ProductGateway;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteProductUseCase Unit Tests")
class DeleteProductUseCaseTest {

    @Mock
    private ProductGateway repository;

    @Mock
    private GetProductByIdUseCase getProductByIdUseCase;

    @InjectMocks
    private DeleteProductUseCase deleteProductUseCase;

    @Nested
    @DisplayName("Execute Method Scenarios")
    class ExecuteMethod {

        @Test
        @DisplayName("Should successfully delete the product when it exists")
        void shouldDeleteProductWhenIdExists() {
            // Arrange
            final var existingProduct = ProductFixture.any();
            final var productId = existingProduct.getId();

            given(getProductByIdUseCase.execute(productId))
                    .willReturn(existingProduct);

            // Act
            deleteProductUseCase.execute(productId);

            // Assert
            verify(getProductByIdUseCase).execute(productId);
            verify(repository).deleteById(productId);
        }

        @Test
        @DisplayName("Should throw NotFoundException and never call repository when product does not exist")
        void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
            // Arrange
            final var productId = UUID.randomUUID();

            given(getProductByIdUseCase.execute(productId))
                    .willThrow(NotFoundException.create("Product not found"));

            // Act & Assert
            assertThatThrownBy(() -> deleteProductUseCase.execute(productId))
                    .isInstanceOf(NotFoundException.class);

            verify(getProductByIdUseCase).execute(productId);
            verify(repository, never()).deleteById(productId);
        }
    }
}