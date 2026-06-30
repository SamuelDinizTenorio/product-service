package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Orchestrates the business logic for updating an existing product within the
 * system.
 * <p>
 * This use case acts as a domain boundary component that fetches the current
 * state of a product, mutates its attributes using the provided data tracking
 * business invariants, and updates the state through the persistence layer.
 */
@AllArgsConstructor
public class UpdateProductUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * The internal use case dependency utilized to retrieve the current state of a
     * product.
     */
    private final GetProductByIdUseCase getProductByIdUseCase;

    /**
     * Executes the update lifecycle for a specific product entity.
     * <p>
     * Fetches the current entity using the given identifier, applies the new state
     * attributes from {@code productData}, and commits the changes to the
     * underlying storage.
     *
     * @param id          the unique identifier of the product to be updated; must
     *                    not be {@code null}
     * @param productData the {@link Product} entity carrying the new field values
     *                    to apply to the existing record
     * @return the updated {@link Product} entity after persistent storage
     *         synchronization
     * @throws RuntimeException if the product with the specified {@code id} cannot
     *                          be found
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
