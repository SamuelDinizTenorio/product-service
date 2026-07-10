package com.samuel.productservice.core.usecase;

import com.samuel.productservice.core.exception.NotFoundException;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for retrieving a product aggregate by its unique
 * business code (SKU).
 * <p>
 * This component bridges application entry points to the relational storage
 * layer to fetch active business records matching specific domain keys.
 */
@AllArgsConstructor
public class GetProductBySkuUseCase {

    private final ProductRepository repository;

    /**
     * Executes the operational query to locate a product aggregate by its unique
     * business SKU.
     *
     * @param sku the unique stock keeping unit identifier;
     *            must not be {@code null} or empty
     * @return the reconstituted {@link Product} aggregate associated with
     *         the provided business code
     * @throws NotFoundException if no product aggregate matches
     *                           the specified {@code sku}
     */
    public Product execute(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> NotFoundException.create("Product not found with SKU: %s".formatted(sku)));
    }
}
