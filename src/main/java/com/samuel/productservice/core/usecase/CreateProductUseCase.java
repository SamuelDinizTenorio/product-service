package com.samuel.productservice.core.usecase;

import java.util.ArrayList;

import com.samuel.productservice.core.exception.ConflictException;
import com.samuel.productservice.core.exception.DomainException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the creation and persistence of a new
 * product.
 * <p>
 * This class acts as a domain boundary, receiving data for a new product and
 * delegating validation and persistence to the appropriate layers (domain model
 * and repository).
 */
@AllArgsConstructor
public class CreateProductUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * Executes the use case to create and save a new product.
     * <p>
     * The creation of the {@link Product} entity itself enforces the necessary
     * domain validations. If the data is valid, the new entity is passed to the
     * {@link ProductRepository} to be persisted.
     *
     * @param newProduct the {@link Product} entity representing the new product
     *                   details to be persisted; must not be {@code null}
     * @return the persisted {@link Product} entity, which may include
     *         system-generated attributes (such as the ID)
     * @throws ConflictException if a product with the same stock keeping unit (SKU)
     *                           Already exists
     */
    public Product execute(Product newProduct) {
        if (repository.findBySku(newProduct.getSku()).isPresent()) {
            throw ConflictException.create("Product with SKU %s already exists".formatted(newProduct.getSku()));
        }

        return repository.save(newProduct);
    }
}
