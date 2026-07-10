package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the structural update of an existing
 * product.
 * <p>
 * This component manages the sequential flow of state mutations by recovering
 * the active domain aggregate, applying safe field updates, and synchronizing
 * changes back to the storage layer.
 */
@AllArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository repository;
    private final GetProductByIdUseCase getProductByIdUseCase;

    /**
     * Executes the business state modification sequence on an existing product
     * aggregate.
     *
     * @param id          the unique {@link UUID} of the product aggregate to
     *                    modify; must not be {@code null}
     * @param productData the {@link Product} template aggregate containing the
     *                    fresh state transformations to apply
     * @return the fully updated and persisted {@link Product} domain aggregate
     * @throws NotFoundException if the targeted product aggregate cannot be
     *                           identified by the provided {@code id}
     */
    public Product execute(UUID id, Product productData) {
        var existingProduct = getProductByIdUseCase.execute(id);

        existingProduct.update(
                productData.getSku(),
                productData.getName(),
                productData.getStock(),
                productData.getCost(),
                productData.getPrice());

        return repository.update(existingProduct);
    }
}
