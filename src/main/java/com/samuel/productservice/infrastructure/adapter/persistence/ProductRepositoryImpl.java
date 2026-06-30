package com.samuel.productservice.infrastructure.adapter.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.repository.ProductRepository;
import com.samuel.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;

import lombok.AllArgsConstructor;

/**
 * Infrastructure repository adapter implementing the core domain
 * {@link ProductRepository} interface.
 * <p>
 * Following the Ports and Adapters (Hexagonal) architecture pattern, this
 * component acts as a secondary adapter that fulfills the persistence port by
 * orchestrating interactions between a Spring Data JPA database repository
 * ({@link JpaProductRepository}) and a structural mapping layer
 * ({@link ProductMapper}).
 */
@Component
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    /**
     * The Spring Data JPA data access layer providing low-level relational
     * operations.
     */
    private final JpaProductRepository repository;

    /**
     * The utility mapper used to transform structures across layer boundaries.
     */
    private final ProductMapper mapper;

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
    public Optional<Product> findById(UUID id) {
        return repository.findById(id)
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
    public Product save(Product product) {
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
    public Product update(Product product) {
        var entity = mapper.toUpdateEntity(product);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
