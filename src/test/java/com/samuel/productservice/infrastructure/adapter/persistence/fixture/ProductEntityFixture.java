package com.samuel.productservice.infrastructure.adapter.persistence.fixture;

import com.samuel.productservice.infrastructure.adapter.persistence.entity.ProductEntity;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Test fixture factory providing pre-configured {@link ProductEntity} instances
 * for testing purposes.
 * <p>
 * This fixture simplifies test setup by offering standardized default datasets
 * and customization methods, minimizing test maintenance when the underlying
 * database entity structure changes.
 */
@UtilityClass
public class ProductEntityFixture {

    /**
     * Creates a {@link ProductEntity} instance populated with valid default data
     * and a randomized identifier.
     *
     * @return a valid {@link ProductEntity} with a randomly generated {@link UUID}
     *         and isNew set to true
     */
    public ProductEntity any() {
        return createWithId(UUID.randomUUID());
    }

    /**
     * Creates a {@link ProductEntity} instance with a specified identifier and
     * default attribute values.
     *
     * @param id the unique identifier to assign to the entity; must not be
     *           {@code null}
     * @return a {@link ProductEntity} with the specified {@code id}, default
     *         attributes, and isNew set to true
     */
    public ProductEntity createWithId(final UUID id) {
        return ProductEntity.create(
                id,
                "SKU-DEFAULT",
                "Default Product Name",
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.valueOf(20),
                true);
    }

    /**
     * Creates a {@link ProductEntity} instance with a specified identifier and
     * explicit persistence state.
     * <p>
     * Useful for testing database synchronization logic or lifecycle listeners
     * where the
     * tracking state of the entity dictates the framework behavior.
     *
     * @param id    the unique identifier to assign to the entity; must not be
     *              {@code null}
     * @param isNew {@code true} if the entity should be treated as a new, unsaved
     *              record; {@code false} otherwise
     * @return a {@link ProductEntity} configured with the specified {@code id} and
     *         {@code isNew} state
     */
    public ProductEntity createWithIdAndIsNew(final UUID id, final boolean isNew) {
        return ProductEntity.create(
                id,
                "SKU-DEFAULT",
                "Default Product Name",
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.valueOf(20),
                isNew);
    }
}
