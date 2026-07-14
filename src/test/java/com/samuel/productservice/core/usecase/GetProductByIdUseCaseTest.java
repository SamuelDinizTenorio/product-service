package com.samuel.productservice.core.usecase;

import java.util.Optional;
import java.util.UUID;

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
@DisplayName("GetProductByIdUseCase Unit Tests")
class GetProductByIdUseCaseTest {

        @Mock
        private ProductGateway repository;

        @InjectMocks
        private GetProductByIdUseCase getProductByIdUseCase;

        @Nested
        @DisplayName("Execute Method Scenarios")
        class ExecuteMethod {

                @Test
                @DisplayName("Should successfully return the product when it exists in the repository")
                void shouldReturnProductWhenProductExists() {
                        // Arrange
                        final var expectedProduct = ProductFixture.any();
                        final var productId = expectedProduct.getId();

                        given(repository.findById(productId))
                                        .willReturn(Optional.of(expectedProduct));

                        // Act
                        Product actualProduct = getProductByIdUseCase.execute(productId);

                        // Assert
                        assertThat(actualProduct)
                                        .isNotNull()
                                        .isSameAs(expectedProduct);
                        verify(repository).findById(productId);
                }

                @Test
                @DisplayName("Should throw NotFoundException when the product does not exist in the repository")
                void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
                        // Arrange
                        UUID productId = UUID.randomUUID();
                        given(repository.findById(productId))
                                        .willReturn(Optional.empty());

                        // Act & Assert
                        assertThatThrownBy(() -> getProductByIdUseCase.execute(productId))
                                        .isInstanceOf(NotFoundException.class)
                                        .hasMessageContaining(productId.toString());
                        verify(repository).findById(productId);
                }
        }
}
