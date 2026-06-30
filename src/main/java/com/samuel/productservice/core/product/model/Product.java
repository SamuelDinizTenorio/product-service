package com.samuel.productservice.core.product.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import com.samuel.productservice.core.exception.NotificationException;
import com.samuel.productservice.core.validation.NotificationError;
import com.samuel.productservice.core.validation.NotificationValidation;

import lombok.Getter;

/**
 * Represents a product domain entity with business rule validation.
 * <p>
 * This entity enforces core domain invariants such as mandatory attributes,
 * string length thresholds, and transactional consistency between cost and
 * price.
 */
@Getter
public class Product {

    /**
     * The unique identifier of the product.
     */
    private UUID id;

    /**
     * The stock keeping unit identifier.
     */
    private String sku;

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
     * Initializes a new {@link Product} with all specified fields and executes
     * self-validation.
     *
     * @param id    the unique string identifier of the product
     * @param sku   the stock keeping unit; must not be {@code null} or blank
     * @param name  the product name; must be between 5 and 200 characters long when
     *              trimmed, and must not be {@code null}
     * @param stock the initial stock quantity; must be a non-negative value, and
     *              must not be {@code null}
     * @param cost  the unit cost value; must be strictly greater than zero, and
     *              must not be {@code null}
     * @param price the consumer selling price; must be strictly greater than
     *              {@code cost}, and must not be {@code null}
     * @throws NotificationException if any provided argument violates product
     *                               business invariants
     */
    private Product(
            final UUID id,
            final String sku,
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
        selfValidate();
    }

    /**
     * Creates and validates a new {@link Product} instance with a randomly
     * generated unique identifier.
     *
     * @param sku   the stock keeping unit; must not be {@code null} or blank
     * @param name  the product name; must be between 5 and 200 characters long when
     *              trimmed, and must not be {@code null}
     * @param stock the initial stock quantity; must be a non-negative value, and
     *              must not be {@code null}
     * @param cost  the unit cost value; must be strictly greater than zero, and
     *              must not be {@code null}
     * @param price the consumer selling price; must be strictly greater than
     *              {@code cost}, and must not be {@code null}
     * @return a fully validated {@link Product} instance
     * @throws NotificationException if any parameter fails to comply with domain
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
     * Reconstitutes an existing {@link Product} aggregate from a known persistent
     * state.
     * <p>
     * This factory method bypasses new entity initialization workflows, allowing
     * the domain layer to safely reload established state from data stores.
     *
     * @param id    the unique {@link UUID} identifier of the product
     * @param sku   the stock keeping unit identifier
     * @param name  the product name
     * @param stock the current stock quantity
     * @param cost  the unit cost value
     * @param price the commercial selling price
     * @return a {@link Product} instance populated with the specified state
     * @throws NullPointerException if the specified {@code id}
     *                              argument is {@code null}
     */
    public static Product reconstitute(
            final UUID id,
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        Objects.requireNonNull(id, "Product identity (ID) cannot be null during reconstitution");

        return new Product(id, sku, name, stock, cost, price);
    }

    /**
     * Updates the product state attributes and re-evaluates internal invariants.
     *
     * @param sku   the new stock keeping unit; must not be {@code null} or blank
     * @param name  the new product name; must be between 5 and 200 characters long
     *              when trimmed
     *              and must not be {@code null}
     * @param stock the new stock quantity; must be a non-negative value, and
     *              must not be {@code null}
     * @param cost  the new unit cost value; must be strictly greater than zero, and
     *              must not be {@code null}
     * @param price the new consumer selling price; must be strictly greater than
     *              {@code cost}, and must not be {@code null}
     * @throws NotificationException if any updated parameter violates domain
     *                               constraints
     */
    public void update(
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        selfValidate();
    }

    /**
     * Asserts the integrity of all internal product attributes against business
     * rules.
     * <p>
     * Collects all validation violations through a {@link NotificationValidation}
     * container
     * and triggers a single aggregate exception if errors are present.
     *
     * @throws NotificationException if one or more internal fields fail domain
     *                               validations
     */
    private void selfValidate() {
        final var notification = NotificationValidation.create();

        if (this.sku == null || this.sku.isBlank()) {
            notification.append(new NotificationError("sku cannot be null or blank"));
        }

        if (this.name == null) {
            notification.append(new NotificationError("name cannot be null"));
        }
        if (this.name != null) {
            final var nameLength = this.name.trim().length();
            if (nameLength < 5 || nameLength > 200) {
                notification.append(new NotificationError("name must be between 5 and 200 characters"));
            }
        }

        if (this.stock == null) {
            notification.append(new NotificationError("stock cannot be null"));
        }
        if (this.stock != null && this.stock.compareTo(BigDecimal.ZERO) < 0) {
            notification.append(new NotificationError("stock must be positive"));
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
            throw new NotificationException("failed to instance new product", notification);
        }
    }
}