package com.samuel.productservice.core.usecase;

import com.samuel.productservice.core.exception.ConflictException;
import com.samuel.productservice.core.gateway.ProductGateway;
import com.samuel.productservice.core.model.Product;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the creation and persistence of a new
 * product.
 * <p>
 * This component acts as a domain boundary, enforcing business invariants and
 * uniqueness constraints before delegating the persistence operation to the
 * core repository layer.
 */
@AllArgsConstructor
public class CreateProductUseCase {

    private final ProductGateway repository;

    /**
     * Executes the business logic to create and persist a new product aggregate.
     *
     * @param newProduct the {@link Product} domain aggregate containing the new
     *                   product details; must not be {@code null}
     * @return the successfully persisted {@link Product} aggregate reflecting
     *         system-assigned attributes
     * @throws ConflictException if a product with the same stock keeping unit (SKU)
     *                           already exists
     */
    public Product execute(Product newProduct) {
        if (repository.findBySku(newProduct.getSku()).isPresent()) {
            throw ConflictException.create("Product already exists with SKU: %s".formatted(newProduct.getSku()));
        }

        return repository.save(newProduct);
    }
}
