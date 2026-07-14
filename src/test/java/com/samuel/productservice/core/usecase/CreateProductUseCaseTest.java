package com.samuel.productservice.core.usecase;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samuel.productservice.core.exception.ConflictException;
import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.gateway.ProductGateway;
import com.samuel.productservice.core.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductUseCase Unit Tests")
class CreateProductUseCaseTest {

        @Mock
        private ProductGateway repository;

        @InjectMocks
        private CreateProductUseCase createProductUseCase;

        @Nested
        @DisplayName("Execute Method Scenarios")
        class ExecuteMethod {

                @Test
                @DisplayName("Should successfully create and return a new product")
                void shouldCreateAndReturnNewProduct() {
                        // Arrange
                        final var newProduct = ProductFixture.any();
                        final var productSku = newProduct.getSku();

                        given(repository.findBySku(productSku))
                                        .willReturn(Optional.empty());
                        given(repository.save(newProduct))
                                        .willReturn(newProduct);

                        // Act
                        Product createdProduct = createProductUseCase.execute(newProduct);

                        // Assert
                        assertThat(createdProduct)
                                        .isNotNull()
                                        .isSameAs(newProduct);

                        verify(repository).findBySku(productSku);
                        verify(repository).save(newProduct);
                }

                @Test
                @DisplayName("Should throw ConflictException when product SKU already exists")
                void shouldThrowConflictExceptionWhenSkuExists() {
                        // Arrange
                        final var newProduct = ProductFixture.any();
                        final var productSku = newProduct.getSku();

                        given(repository.findBySku(productSku))
                                        .willReturn(Optional.of(newProduct));

                        // Act & Assert
                        assertThatThrownBy(() -> createProductUseCase.execute(newProduct))
                                        .isInstanceOf(ConflictException.class)
                                        .hasMessageContaining(productSku.getValue());

                        verify(repository).findBySku(productSku);
                        verify(repository, never()).save(any());
                }
        }
}
