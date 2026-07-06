package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the deletion of an existing product.
 * <p>
 * This class coordinates the domain logic to ensure a product exists before
 * requesting its removal from the persistence layer.
 */
@AllArgsConstructor
public class DeleteProductUseCase {

    /**
     * The internal use case dependency used to verify the product's existence
     * before deletion.
     */
    private final GetProductByIdUseCase getProductByIdUseCase;

    /**
     * The data access gateway used to perform product deletion operations.
     */
    private final ProductRepository repository;

    /**
     * Executes the use case to delete a product based on its ID.
     *
     * @param id the unique identifier of the product to be deleted; must not be
     *           {@code null}
     * @throws NotFoundException if no product matches the provided {@code id}
     */
    public void execute(UUID id) {
        getProductByIdUseCase.execute(id);
        repository.deleteById(id);
    }
}
