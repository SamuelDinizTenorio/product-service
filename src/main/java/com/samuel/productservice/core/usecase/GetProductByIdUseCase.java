package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for retrieving a product aggregate by its unique
 * identifier.
 * <p>
 * This component acts as a direct query bridge to fetch a single core
 * aggregate, enforcing exceptional boundary flows when a requested resource
 * cannot be recovered.
 */
@AllArgsConstructor
public class GetProductByIdUseCase {

    private final ProductRepository repository;

    /**
     * Executes the resolution query to locate a product aggregate by its unique
     * identifier.
     *
     * @param id the unique {@link UUID} of the target product aggregate;
     *           must not be {@code null}
     * @return the reconstituted {@link Product} aggregate associated with
     *         the specified identifier
     * @throws NotFoundException if no product aggregate matches
     *                           the provide {@code id}
     */
    public Product execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> NotFoundException.create("Product not found with ID: %s".formatted(id)));
    }
}
