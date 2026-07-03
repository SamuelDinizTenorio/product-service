package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for retrieving a product based on its unique identifier
 * (ID).
 * <p>
 * This class coordinates the domain logic to ensure that a product exists for
 * the requested identifier, establishing a bridge between the application entry
 * points and the underlying persistence layer.
 */
@AllArgsConstructor
public class GetProductByIdUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * Executes the use case to find a product matching the specified ID.
     * <p>
     * Queries the underlying {@link ProductRepository} and ensures an instance is
     * returned if found, enforcing that missing records terminate abnormally.
     *
     * @param id the {@link UUID} representing the unique identifier of the target
     *           product; must not be {@code null}
     * @return the {@link Product} entity associated with the provided identifier
     * @throws NotFoundException if no product matches the provided {@code id}
     */
    public Product execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> NotFoundException.create("Product not found with ID: %s".formatted(id)));
    }
}
