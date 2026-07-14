package com.samuel.productservice.infrastructure.adapter.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.samuel.productservice.core.gateway.ProductGateway;
import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.model.Sku;
import com.samuel.productservice.infrastructure.adapter.persistence.mapper.ProductPersistenceMapper;

import lombok.AllArgsConstructor;

/**
 * Infrastructure repository adapter implementing the core domain
 * {@link ProductGateway} interface.
 * <p>
 * Following the Ports and Adapters (Hexagonal) architecture pattern, this
 * component acts as a secondary adapter that fulfills the persistence port by
 * orchestrating interactions between a Spring Data JPA database repository
 * ({@link ProductJpaRepository}) and a structural mapping layer
 * ({@link ProductPersistenceMapper}).
 */
@Component
@AllArgsConstructor
public class ProductJpaAdapter implements ProductGateway {

    /**
     * The Spring Data JPA data access layer providing low-level relational
     * operations.
     */
    private final ProductJpaRepository repository;

    /**
     * The utility mapper used to transform structures across layer boundaries.
     */
    private final ProductPersistenceMapper mapper;

    /**
     * Retrieves a product aggregate from the relational database by its unique
     * identifier.
     *
     * @param id the unique {@link UUID} of the product to find; must not be
     *           {@code null}
     * @return an {@link Optional} containing the reconstituted {@link Product}
     *         aggregate if a matching record exists; an empty {@link Optional}
     *         otherwise
     */
    @Override
    public Optional<Product> findById(final UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    /**
     * Retrieves a product aggregate from the relational database by its stock
     * keeping unit.
     *
     * @param sku the unique {@link String} identifier representing the product SKU;
     *            must not be {@code null}
     * @return an {@link Optional} containing the reconstituted {@link Product}
     *         aggregate if a matching record exists; an empty {@link Optional}
     *         otherwise
     */
    @Override
    public Optional<Product> findBySku(final Sku sku) {
        return repository.findBySku(sku.getValue())
                .map(mapper::toDomain);
    }

    /**
     * Persists the current state of a product aggregate into the relational
     * database.
     * <p>
     * This implementation translates the domain model into a persistence-specific
     * entity, executes a database save operation via the underlying JPA provider,
     * and returns a freshly reconstituted domain aggregate reflecting the persisted
     * records.
     *
     * @param product the {@link Product} domain aggregate to save; must not be
     *                {@code null}
     * @return the saved and reconstituted {@link Product} aggregate instance
     */
    @Override
    public Product save(final Product product) {
        var entity = mapper.toNewEntity(product);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Updates the persistent state of an existing product aggregate in the
     * relational database.
     * <p>
     * This implementation translates the domain model into an update-specific
     * entity configuration (explicitly flagging it as an existing record), executes
     * the update via the underlying JPA provider, and returns a freshly
     * reconstituted domain aggregate reflecting the updated database state.
     *
     * @param product the {@link Product} domain aggregate containing updated state;
     *                must not be {@code null}
     * @return the updated and reconstituted {@link Product} aggregate instance
     */
    @Override
    public Product update(final Product product) {
        var entity = mapper.toUpdateEntity(product);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Deletes a product aggregate from the relational database based on its unique
     * identifier.
     * <p>
     * This implementation delegates the deletion request directly to the
     * underlying {@link ProductJpaRepository}, which handles the physical removal
     * of the corresponding database record. If no product with the specified ID
     * exists, the operation completes silently without error, consistent with the
     * repository contract.
     *
     * @param id the unique {@link UUID} of the product to delete; must not be
     *           {@code null}
     */
    @Override
    public void deleteById(final UUID id) {
        repository.deleteById(id);
    }

    /**
     * Retrieves a paginated collection of all product aggregates from the
     * relational database.
     * <p>
     * This implementation delegates the query to the underlying JPA repository with
     * the provided pagination constraints, then maps each retrieved entity back to
     * its domain representation.
     *
     * @param pageable the pagination and sorting metadata configuration; must not
     *                 be {@code null}
     * @return a {@link Page} containing the reconstituted {@link Product}
     *         aggregates matching the pagination constraints
     */
    @Override
    public Page<Product> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
