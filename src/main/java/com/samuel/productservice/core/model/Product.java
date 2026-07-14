package com.samuel.productservice.core.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import com.samuel.productservice.core.exception.NotificationException;
import com.samuel.productservice.core.validation.NotificationError;
import com.samuel.productservice.core.validation.NotificationValidation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents the Product aggregate root within the core domain layer.
 * <p>
 * This entity encapsulates and enforces essential business invariants, ensuring
 * structural consistency for mandatory attributes, string constraints, and
 * valid monetary relationships between product costs and commercial prices.
 * <p>
 * <strong>Identity and Equality:</strong> In compliance with Domain-Driven
 * Design (DDD) conventions, identity and equality definitions ({@code equals}
 * and {@code hashCode}) are strictly bound to the immutable unique identifier
 * ({@code id}), persisting uniformly across any state transitions or mutations.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    /**
     * The unique identifier of the product.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * The encapsulated Stock Keeping Unit (SKU) value object.
     */
    private Sku sku;

    /**
     * The descriptive name of the product.
     */
    private String name;

    /**
     * The available quantity in inventory stock.
     */
    private BigDecimal stock;

    /**
     * The acquisition or production cost of the product.
     */
    private BigDecimal cost;

    /**
     * The commercial selling price of the product.
     */
    private BigDecimal price;

    /**
     * Standard internal constructor used for new instances and state updates.
     * Executes self-validation routines upon assignment.
     *
     * @param id     the unique identifier of the product
     * @param rawSku the raw, unvalidated string representation of the SKU
     * @param name   the descriptive name of the product
     * @param stock  the inventory stock quantity
     * @param cost   the unit acquisition cost
     * @param price  the commercial selling price
     * @throws NotificationException if any domain invariant constraint is violated
     */
    private Product(
            final UUID id,
            final String rawSku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        selfValidate(rawSku);
    }

    /**
     * Technical internal constructor dedicated exclusively to state reconstitution
     * workflows.Bypasses active validation checkpoints to load verified historical
     * states safely.
     *
     * @param id    the unique identifier of the product
     * @param sku   the pre-validated {@link Sku} value object
     * @param name  the descriptive name of the product
     * @param stock the current inventory stock quantity
     * @param cost  the unit acquisition cost
     * @param price the commercial selling price
     */
    private Product(
            final UUID id,
            final Sku sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
    }

    /**
     * Factory method to initialize and validate a new {@link Product} aggregate
     * instance with a randomly assigned unique identifier.
     *
     * @param sku   the raw SKU string identifier; must not be null or blank
     * @param name  the product name; must contain between 5 and 200 characters
     *              when trimmed
     * @param stock the initial stock quantity; must be a non-negative value
     * @param cost  the unit cost; must be strictly greater than zero
     * @param price the consumer selling price; must be strictly greater than the
     *              unit cost
     * @return a fully validated and instantiated {@link Product} aggregate root
     * @throws NotificationException if any parameter fails to satisfy domain
     *                               constraints
     */
    public static Product create(
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        final var id = UUID.randomUUID();
        return new Product(id, sku, name, stock, cost, price);
    }

    /**
     * Reconstitutes an established {@link Product} aggregate instance from a
     * persistent data store.
     * <p>
     * This method bypasses domain lifecycle checks, enabling the persistence
     * mechanism to safely remap historical data states back into the domain layer.
     * </p>
     *
     * @param id    the persistent unique identifier of the product
     * @param sku   the persistent {@link Sku} value object;
     *              must not be {@code null}
     * @param name  the persistent product name
     * @param stock the persistent stock quantity
     * @param cost  the persistent cost value
     * @param price the persistent price value
     * @return a trusted {@link Product} instance populated with the persistent
     *         state
     * @throws NullPointerException if the required {@code id} reference is null
     */
    public static Product reconstitute(
            final UUID id,
            final Sku sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        Objects.requireNonNull(id, "Product identity (ID) cannot be null during reconstitution");
        return new Product(id, sku, name, stock, cost, price);
    }

    /**
     * Mutates mutable product attributes and re-evaluates all core domain
     * invariants.
     *
     * @param sku   the updated raw SKU identifier string; must not be null or blank
     * @param name  the updated product name; must be between 5 and 200 characters
     *              when trimmed
     * @param stock the updated stock quantity; must be a non-negative value
     * @param cost  the updated unit cost; must be strictly greater than zero
     * @param price the updated consumer selling price; must be strictly greater
     *              than the cost
     * @throws NotificationException if the newly provided state violates domain
     *                               constraints
     */
    public void update(
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        this.name = name != null ? name.trim() : null;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        selfValidate(sku);
    }

    /**
     * Asserts internal field state validity against business specifications.
     * <p>
     * Utilizes a notification container to collect all validation errors found
     * across fields, throwing a consolidated exception rather than failing
     * instantly on the first error.
     * </p>
     *
     * @param rawSku the raw, unvalidated string representation of the SKU
     * @throws NotificationException if one or more internal attributes fail
     *                               validation checks
     */
    private void selfValidate(final String rawSku) {
        final var notification = NotificationValidation.create();

        // Delegates SKU parsing and validation to its respective value object
        this.sku = Sku.of(rawSku, notification);

        if (this.name == null) {
            notification.append(new NotificationError("name cannot be null"));
        } else {
            final var nameLength = this.name.length();
            if (nameLength < 5 || nameLength > 200) {
                notification.append(new NotificationError("name must be between 5 and 200 characters"));
            }
        }

        if (this.stock == null) {
            notification.append(new NotificationError("stock cannot be null"));
        }
        if (this.stock != null && this.stock.compareTo(BigDecimal.ZERO) < 0) {
            notification.append(new NotificationError("stock cannot be negative"));
        }

        if (this.cost == null) {
            notification.append(new NotificationError("cost cannot be null"));
        }
        if (this.cost != null && this.cost.compareTo(BigDecimal.ZERO) <= 0) {
            notification.append(new NotificationError("cost must be greater than zero"));
        }

        if (this.price == null) {
            notification.append(new NotificationError("price cannot be null"));
        }
        if (this.price != null && this.cost != null && this.price.compareTo(this.cost) <= 0) {
            notification.append(new NotificationError("price must be greater than cost"));
        }

        if (notification.hasErrors()) {
            throw new NotificationException("Product validation failed", notification);
        }
    }
}