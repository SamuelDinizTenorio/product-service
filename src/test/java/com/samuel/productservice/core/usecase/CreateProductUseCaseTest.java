package com.samuel.productservice.core.usecase;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductUseCase Unit Tests")
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    @Nested
    @DisplayName("Execute Method Scenarios")
    class ExecuteMethod {
        @Test
        @DisplayName("Should successfully create and return a new product")
        void shouldCreateAndReturnNewProduct() {
            // Arrange
            final var newProduct = Product.create(
                    "SKU-123",
                    "Teclado Mecânico",
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(150.00),
                    BigDecimal.valueOf(299.90));

            given(repository.save(newProduct))
                    .willReturn(newProduct);

            // Act
            Product createdProduct = createProductUseCase.execute(newProduct);

            // Assert
            assertThat(createdProduct)
                    .isNotNull()
                    .isSameAs(newProduct);
            verify(repository).save(newProduct);
        }
    }
}
