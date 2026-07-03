package com.samuel.productservice.core.usecase;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for retrieving a product based on its unique stock
 * keeping unit (SKU).
 * <p>
 * This class coordinates the domain logic to ensure that a product exists for
 * the requested
 * identifier, establishing a bridge between the application entry points and
 * the underlying persistence layer.
 */
@AllArgsConstructor
public class GetProductBySkuUseCase {

    /**
     * The data access gateway used to query product records.
     */
    private final ProductRepository repository;

    /**
     * Executes the use case to find a product matching the specified SKU.
     * <p>
     * Queries the underlying {@link ProductRepository} and ensures an instance is
     * returned
     * if found, enforcing that missing records terminate abnormally.
     *
     * @param sku the unique stock keeping unit identifier, must not be {@code null}
     *            or empty
     * @return the {@link Product} entity associated with the given identifier
     * @throws NotFoundException if no product matches the provided {@code sku}
     */
    public Product execute(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> NotFoundException.create("Product not found with SKU: %s".formatted(sku)));
    }
}
