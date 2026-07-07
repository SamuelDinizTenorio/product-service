package com.samuel.productservice.infrastructure.adapter.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Persistent database entity representing a product record within the
 * relational schema.
 * <p>
 * This class implements Spring Data's {@link Persistable} interface to
 * explicitly handle the differentiation between database insert operations and
 * update operations, which is necessary when using pre-assigned identifiers
 * like {@link UUID}.
 */
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductEntity implements Persistable<UUID> {

    /**
     * The primary key identifier of the product.
     * <p>
     * Stored as a 36-character string representation of the {@link java.util.UUID}
     * to maintain readability and compatibility within the MySQL schema.
     */
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "VARCHAR(36)")
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * The stock keeping unit identifier.
     */
    private String sku;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The inventory quantity available.
     */
    private BigDecimal stock;

    /**
     * The baseline production or purchase cost.
     */
    private BigDecimal cost;

    /**
     * The retail or commercial sales price.
     */
    private BigDecimal price;

    /**
     * Internal lifecycle flag indicating whether this entity represents a new
     * record.
     * <p>
     * Defaults to {@code true} and is not persisted in the database schema.
     */
    @Transient
    private boolean isNew = true;

    /**
     * Factory method to create a parameterized instance of {@link ProductEntity}.
     *
     * @param id    the unique identifier of the product
     * @param sku   the stock keeping unit code
     * @param name  the name of the product
     * @param stock the initial or current inventory stock
     * @param cost  the baseline unit cost
     * @param price the current consumer selling price
     * @param isNew {@code true} if this entity is recognized as a new record to be
     *              inserted, {@code false} if it represents an existing database
     *              state
     * @return a new {@link ProductEntity} instance populated with the provided
     *         parameters
     */
    public static ProductEntity create(
            final UUID id,
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price,
            final boolean isNew) {
        return new ProductEntity(id, sku, name, stock, cost, price, isNew);
    }

    /**
     * Lifecycle callback executed by the JPA provider after loading the entity from
     * the database.
     * <p>
     * This method resets the {@link #isNew} flag to {@code false} to indicate that
     * the entity is already managed and exists within the data store.
     */
    @PostLoad
    protected void postLoad() {
        this.isNew = false;
    }

    /**
     * Checks if the entity is a new database entry or an existing record.
     *
     * @return {@code true} if the entity represents a new entry that has not yet
     *         been saved to the database; {@code false} otherwise
     */
    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
