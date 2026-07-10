package com.samuel.productservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samuel.productservice.core.repository.ProductRepository;
import com.samuel.productservice.core.usecase.CreateProductUseCase;
import com.samuel.productservice.core.usecase.DeleteProductUseCase;
import com.samuel.productservice.core.usecase.FindAllProductUseCase;
import com.samuel.productservice.core.usecase.GetProductByIdUseCase;
import com.samuel.productservice.core.usecase.GetProductBySkuUseCase;
import com.samuel.productservice.core.usecase.UpdateProductUseCase;

/**
 * Spring configuration class responsible for instantiating and configuring
 * domain use cases.
 * <p>
 * This class serves as a central factory configuration to inject the required
 * dependencies, such as infrastructure repositories, into the core business
 * logic components.
 */
@Configuration
public class ProductUseCaseConfig {

    /**
     * Instantiates the use case for retrieving a product by its unique
     * identifier.
     *
     * @param repository the {@link ProductRepository} used by the use case to query
     *                   data
     * @return an initialized instance of {@link GetProductByIdUseCase}
     */
    @Bean
    public GetProductByIdUseCase getProductByIdUseCase(ProductRepository repository) {
        return new GetProductByIdUseCase(repository);
    }

    /**
     * Instantiates the use case for retrieving a product by its unique stock
     * keeping unit.
     *
     * @param repository the {@link ProductRepository} used by the use case to query
     *                   data; must not be {@code null}
     * @return an initialized instance of {@link GetProductBySkuUseCase}
     */
    @Bean
    public GetProductBySkuUseCase getProductBySkuUseCase(ProductRepository repository) {
        return new GetProductBySkuUseCase(repository);
    }

    /**
     * Instantiates the use case responsible for handling product creation
     * logic.
     *
     * @param repository the {@link ProductRepository} used by the use case to
     *                   persist new items
     * @return an initialized instance of {@link CreateProductUseCase}
     */
    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository repository) {
        return new CreateProductUseCase(repository);
    }

    /**
     * Instantiates the use case responsible for updating existing product
     * information.
     *
     * @param repository            the {@link ProductRepository} used by the use
     *                              case to save changes
     * @param getProductByIdUseCase the {@link GetProductByIdUseCase} dependency
     *                              required to look up the target product before
     *                              applying modifications
     * @return an initialized instance of {@link UpdateProductUseCase}
     */
    @Bean
    public UpdateProductUseCase updateProductUseCase(
            ProductRepository repository,
            GetProductByIdUseCase getProductByIdUseCase) {
        return new UpdateProductUseCase(repository, getProductByIdUseCase);
    }

    /**
     * Instantiates the use case responsible for handling product deletion logic.
     *
     * @param repository            the {@link ProductRepository} used by the use
     *                              case to execute the deletion
     * @param getProductByIdUseCase the {@link GetProductByIdUseCase} dependency
     *                              required to verify the product's existence
     *                              before attempting to delete it
     * @return an initialized instance of {@link DeleteProductUseCase}
     */
    @Bean
    public DeleteProductUseCase deleteProductUseCase(
            ProductRepository repository,
            GetProductByIdUseCase getProductByIdUseCase) {
        return new DeleteProductUseCase(getProductByIdUseCase, repository);
    }

    /**
     * Instantiates the use case responsible for retrieving a paginated list of
     * products.
     *
     * @param repository the {@link ProductRepository} used by the
     *                   use case to query data
     * @return an initialized instance of {@link FindAllProductUseCase}
     */
    @Bean
    public FindAllProductUseCase findAllProductUseCase(ProductRepository repository) {
        return new FindAllProductUseCase(repository);
    }
}
