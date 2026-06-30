package com.samuel.productservice.core.usecase;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Orchestrates the business logic for creating and persisting a new product in
 * the system.
 * <p>
 * This use case serves as a domain boundary component that validates the
 * creation contract and delegates the persistence operations to the underlying
 * repository layer.
 */
@AllArgsConstructor
public class CreateProductUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * Executes the business logic to create and save a new product.
     *
     * @param newProduct the {@link Product} entity representing the new product
     *                   details to be persisted; must not be {@code null}
     * @return the persisted {@link Product} entity, populated with any
     *         system-generated attributes
     */
    public Product execute(Product newProduct) {
        return repository.save(newProduct);
    }
}
