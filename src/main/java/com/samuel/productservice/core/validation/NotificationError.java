package com.samuel.productservice.core.validation;

/**
 * Represents a validation error captured within a domain validation context.
 * <p>
 * This record serves as an immutable data carrier for a single error message
 * generated during business rule verification.
 *
 * @param message the descriptive error message detailing the specific
 *                validation failure
 */
public record NotificationError(String message) {

}