package com.samuel.productservice.core.usecase;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.samuel.productservice.core.fixture.ProductFixture;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindAllProductUseCase Unit Tests")
class FindAllProductUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private FindAllProductUseCase findAllProductUseCase;

    @Nested
    @DisplayName("Execute Method Scenarios")
    class ExecuteMethod {

        @Test
        @DisplayName("Should successfully return a paginated list of products")
        void shouldReturnPaginatedProducts() {
            // Arrange
            final var pageable = PageRequest.of(0, 10);
            final var sampleProduct = ProductFixture.any();
            final var expectedPage = new PageImpl<>(List.of(sampleProduct), pageable, 1);

            given(repository.findAll(pageable))
                    .willReturn(expectedPage);

            // Act
            final var actualPage = findAllProductUseCase.execute(pageable);

            // Assert
            assertThat(actualPage)
                    .isNotNull()
                    .hasSize(1)
                    .containsExactly(sampleProduct);

            assertThat(actualPage.getTotalElements()).isEqualTo(1);
            assertThat(actualPage.getTotalPages()).isEqualTo(1);

            verify(repository).findAll(pageable);
        }

        @Test
        @DisplayName("Should return an empty page when no products exist")
        void shouldReturnEmptyPageWhenNoProductsExist() {
            // Arrange
            final var pageable = PageRequest.of(0, 10);
            final var expectedPage = new PageImpl<>(List.<Product>of(), pageable, 0);

            given(repository.findAll(pageable))
                    .willReturn(expectedPage);

            // Act
            final var actualPage = findAllProductUseCase.execute(pageable);

            // Assert
            assertThat(actualPage)
                    .isNotNull()
                    .isEmpty();

            assertThat(actualPage.getTotalElements()).isEqualTo(0);
            assertThat(actualPage.getTotalPages()).isEqualTo(0);

            verify(repository).findAll(pageable);
        }
    }
}
