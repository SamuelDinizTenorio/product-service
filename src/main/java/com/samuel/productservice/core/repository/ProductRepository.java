package com.samuel.productservice.core.repository;

import java.util.Optional;
import java.util.UUID;

import com.samuel.productservice.core.model.Product;

/**
 * Defines the architectural contract for persisting and retrieving
 * {@link Product} aggregates.
 * <p>
 * Implementations of this interface manage the lifecycle abstraction of
 * products, decoupling the core business logic from underlying data access
 * technologies and infrastructures.
 */
public interface ProductRepository {

    /**
     * Retrieves a product aggregate by its unique identifier.
     *
     * @param id the unique {@link UUID} of the product to find; must not be
     *           {@code null}
     * @return an {@link Optional} containing the matching {@link Product}, or an
     *         empty {@link Optional} if no product is found with the specified
     *         identifier
     */
    Optional<Product> findById(UUID id);

    /**
     * Persists the state of the given product aggregate.
     * <p>
     * Implementations must handle both initial insertion and updates of existing
     * products, ensuring that the underlying storage accurately mirrors the domain
     * aggregate's invariants.
     *
     * @param product the {@link Product} domain aggregate to persist; must not be
     *                {@code null}
     * @return the persisted {@link Product} aggregate instance reflecting any
     *         persistence-tier side effects
     */
    Product save(Product product);

    /**
     * Updates the persistent state of an existing product aggregate.
     * <p>
     * This method assumes that the product already exists in the underlying
     * storage and modifies its state according to the provided domain object.
     *
     * @param product the {@link Product} domain aggregate containing the updated
     *                state to persist; must not be {@code null}
     * @return the updated {@link Product} aggregate instance reflecting any
     *         persistence-tier side effects
     */
    Product update(Product product);
}
