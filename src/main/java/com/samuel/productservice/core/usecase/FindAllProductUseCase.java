package com.samuel.productservice.core.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samuel.productservice.core.repository.ProductRepository;
import com.samuel.productservice.core.model.Product;

import lombok.AllArgsConstructor;

/**
 * Use case responsible for retrieving a paginated collection of products.
 * <p>
 * This component coordinates with the core repository to fetch product
 * aggregates matching the specified pagination and sorting constraints.
 */
@AllArgsConstructor
public class FindAllProductUseCase {

    private ProductRepository repository;

    /**
     * Executes the paginated query to retrieve product aggregates.
     *
     * @param pageable the pagination and sorting metadata configuration;
     *                 must not be {@code null}
     * @return a {@link Page} containing the reconstituted {@link Product}
     *         aggregates
     */
    public Page<Product> execute(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
