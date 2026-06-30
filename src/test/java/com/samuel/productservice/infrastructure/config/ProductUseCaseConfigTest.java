package com.samuel.productservice.infrastructure.config;

import com.samuel.productservice.core.usecase.CreateProductUseCase;
import com.samuel.productservice.core.usecase.GetProductByIdUseCase;
import com.samuel.productservice.core.usecase.UpdateProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("ProductUseCaseConfig Test")
public class ProductUseCaseConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRegisterAllUseCaseBeansInApplicationContext() {
        // Act & Assert
        assertThat(context.getBean(GetProductByIdUseCase.class)).isNotNull();
        assertThat(context.getBean(CreateProductUseCase.class)).isNotNull();
        assertThat(context.getBean(UpdateProductUseCase.class)).isNotNull();
    }
}
