package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Orchestrates the business logic for retrieving a specific product by its
 * unique identifier.
 * <p>
 * This use case serves as a boundary component that interacts with the domain's
 * persistence layer to guarantee product existence before processing.
 */
@AllArgsConstructor
public class GetProductByIdUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * Executes the business logic to locate and return a product.
     *
     * @param id the {@link UUID} representing the unique identifier of the target
     *           product; must not be {@code null}
     * @return the {@link Product} entity associated with the provided identifier
     * @throws NotFoundException if no product is found matching the given
     *                           {@code id}
     */
    public Product execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> NotFoundException.create("Product not found with ID: %s".formatted(id)));
    }
}
