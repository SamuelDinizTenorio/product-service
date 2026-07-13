package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.gateway.ProductGateway;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the deletion of an existing product.
 * <p>
 * This component coordinates domain logic to guarantee that a target product
 * aggregate exists within the system before triggering its physical removal via
 * the persistence layer.
 */
@AllArgsConstructor
public class DeleteProductUseCase {

    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ProductGateway repository;

    /**
     * Executes the business logic to remove a product aggregate by its unique
     * identifier.
     *
     * @param id the unique {@link UUID} identifier of the target product to be
     *           deleted; must not be {@code null}
     * @throws NotFoundException if no product aggregate matches the specified
     *                           {@code id}
     */
    public void execute(UUID id) {
        getProductByIdUseCase.execute(id);
        repository.deleteById(id);
    }
}
