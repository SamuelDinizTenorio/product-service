package com.samuel.productservice.infrastructure.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.infrastructure.adapter.persistence.entity.ProductEntity;

/**
 * Component-based mapper service to translate product data between
 * application layers.
 * <p>
 * Managed as a Spring bean, this class handles transformation between the core
 * domain {@link Product} model and the infrastructure data model
 * {@link ProductEntity}, maintaining a clean separation of concerns between
 * domain rules and persistence layouts.
 */
@Component
public class ProductPersistenceMapper {

    /**
     * Transforms a core domain {@link Product} model into a new
     * persistence-oriented {@link ProductEntity}.
     * <p>
     * The resulting entity is explicitly flagged as a new record to instruct the
     * persistence layer to perform an insertion workflow.
     *
     * @param product the core domain model instance to map; can be {@code null}
     * @return a new {@link ProductEntity} flagged for database insertion,
     *         or {@code null} if the supplied {@code product} argument is
     *         {@code null}
     */
    public ProductEntity toNewEntity(final Product product) {
        if (product == null)
            return null;

        return ProductEntity.create(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getStock(),
                product.getCost(),
                product.getPrice(),
                true);
    }

    /**
     * Transforms a core domain {@link Product} model into a persistence-oriented
     * {@link ProductEntity} for updates.
     * <p>
     * The resulting entity is explicitly flagged to indicate that it tracks
     * modifications to an
     * existing record rather than a fresh record insertion.
     *
     * @param product the core domain model instance to map; can be {@code null}
     * @return a new {@link ProductEntity} flagged for database update operations,
     *         or {@code null} if the supplied {@code product} argument is
     *         {@code null}
     */
    public ProductEntity toUpdateEntity(final Product product) {
        if (product == null)
            return null;

        return ProductEntity.create(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getStock(),
                product.getCost(),
                product.getPrice(),
                false);
    }

    /**
     * Transforms a persistence {@link ProductEntity} into a reconstituted core
     * domain {@link Product}.
     * <p>
     * Reconstitution is leveraged to reload existing state into the domain
     * aggregate without re-triggering new instance initialization workflows.
     *
     * @param productEntity the persistent entity to map from; can be {@code null}
     * @return a loaded {@link Product} domain aggregate instance,
     *         or {@code null} if the supplied {@code productEntity} argument is
     *         {@code null}
     */
    public Product toDomain(final ProductEntity productEntity) {
        if (productEntity == null)
            return null;

        return Product.reconstitute(
                productEntity.getId(),
                productEntity.getSku(),
                productEntity.getName(),
                productEntity.getStock(),
                productEntity.getCost(),
                productEntity.getPrice());
    }
}
