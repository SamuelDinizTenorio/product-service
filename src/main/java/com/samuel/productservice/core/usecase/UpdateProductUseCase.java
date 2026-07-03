package com.samuel.productservice.core.usecase;

import java.util.UUID;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for orchestrating the update of an existing product.
 * <p>
 * This class coordinates the domain logic to ensure a product is first
 * retrieved, then modified with new data (respecting domain invariants), and
 * finally persisted back to the system.
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
     * Executes the use case to update a product based on its ID.
     * <p>
     * Retrieves the existing {@link Product} entity, applies the changes from
     * {@code productData}, and delegates the update operation to the
     * {@link ProductRepository}. Domain validation rules are enforced during the
     * entity's {@code update} method call.
     *
     * @param id          the unique identifier of the product to be updated; must
     *                    not be {@code null}
     * @param productData the {@link Product} entity carrying the new field values
     *                    to apply to the existing record
     * @return the updated {@link Product} entity after persistent storage
     *         synchronization
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
