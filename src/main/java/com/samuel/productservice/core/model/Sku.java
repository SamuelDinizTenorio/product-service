package com.samuel.productservice.core.model;

import java.util.regex.Pattern;

import com.samuel.productservice.core.validation.NotificationError;
import com.samuel.productservice.core.validation.NotificationValidation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a Product's Stock Keeping Unit (SKU) as an immutable Value Object.
 * Enforces real-world structural constraints on the SKU string format.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Sku {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z0-9-_]+$");

    @EqualsAndHashCode.Include
    private final String value;

    private Sku(final String value) {
        this.value = value;
    }

    /**
     * Creates and validates a new {@link Sku} instance, accumulating errors
     * into the provided notification container if rules are violated.
     *
     * @param rawValue     the raw SKU string input
     * @param notification the validation container to accumulate errors
     * @return a valid Sku instance, or null if errors are encountered
     */
    public static Sku of(final String rawValue, final NotificationValidation notification) {
        if (rawValue == null || rawValue.isBlank()) {
            notification.append(new NotificationError("sku cannot be null or blank"));
            return null;
        }

        final var cleanedValue = rawValue.trim().toUpperCase();
        final var length = cleanedValue.length();

        if (length < 3 || length > 30) {
            notification.append(new NotificationError("sku must be between 3 and 30 characters"));
        }

        if (!SKU_PATTERN.matcher(cleanedValue).matches()) {
            notification.append(
                    new NotificationError("sku must contain only alphanumeric characters, hyphens, or underscores"));
        }

        return notification.hasErrors() ? null : new Sku(cleanedValue);
    }

    /**
     * Reconstitutes a {@link Sku} instance from a trusted data store without
     * triggering
     * domain validation rules.
     *
     * @param value the persistent SKU value
     * @return a trusted Sku instance
     */
    public static Sku reconstitute(final String value) {
        return new Sku(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}