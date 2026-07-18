package com.samuel.productservice.infrastructure.adapter.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object representing the response payload for a product resource.
 * <p>
 * This record serves as an immutable data carrier, encapsulating the finalized state of a
 * product to be returned across the web application layer boundary.
 *
 * @param id the unique identifier of the product
 * @param sku the unique Stock Keeping Unit identifier
 * @param name the descriptive name of the product
 * @param stock the current inventory quantity available
 * @param cost the financial cost incurred to acquire the product
 * @param price the commercial selling price of the product
 */
public record ProductResponse(
        UUID id,
        String sku,
        String name,
        BigDecimal stock,
        BigDecimal cost,
        BigDecimal price) {
}
