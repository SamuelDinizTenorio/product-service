package com.samuel.productservice.infrastructure.adapter.web.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Data transfer object representing a request to create or update a product.
 * <p>
 * This record acts as an immutable data carrier across the application
 * boundary, validating incoming payload attributes against predefined 
 * domain invariants.
 *
 * @param sku   the unique Stock Keeping Unit identifier;
 *              must be between 3 and 30 characters, contain only alphanumeric
 *              characters, hyphens, or underscores, and cannot be blank
 * @param name  the descriptive name of the product;
 *              must be between 5 and 200 characters and cannot be blank
 * @param stock the initial or updated inventory quantity available;
 *              must not be {@code null} and cannot be negative
 * @param cost  the financial cost incurred to acquire the product;
 *              must not be {@code null} and must be greater than zero
 * @param price the commercial selling price of the product;
 *              must not be {@code null} and must be greater than zero
 */
public record ProductRequest(
        @NotBlank(message = "sku cannot be null or blank") 
        @Size(min = 3, max = 30, message = "sku must be between 3 and 30 characters") 
        @Pattern(regexp = "^[A-Z0-9-_]+$", message = "sku must contain only alphanumeric characters, hyphens, or underscores") 
        String sku,

        @NotBlank(message = "name cannot be null or blank") 
        @Size(min = 5, max = 200, message = "name must be between 5 and 200 characters") 
        String name,

        @NotNull(message = "stock cannot be null") 
        @PositiveOrZero(message = "stock cannot be negative") 
        BigDecimal stock,

        @NotNull(message = "cost cannot be null") 
        @Positive(message = "cost must be greater than zero") 
        BigDecimal cost,

        @NotNull(message = "price cannot be null") 
        @Positive(message = "price must be greater than zero") 
        BigDecimal price) {
}
