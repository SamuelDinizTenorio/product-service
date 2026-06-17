package com.samuel.productservice.core.validation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Collects and accumulates domain validation errors during business operations.
 * <p>
 * This class implements the Notification pattern, allowing multiple validation
 * issues to be captured and evaluated together rather than failing fast on the
 * first error.
 */
@Getter
public class NotificationValidation {

    /**
     * The internal list of accumulated validation errors.
     */
    private List<NotificationError> errors;

    /**
     * Initializes a new notification instance with a predefined list of errors.
     *
     * @param errors the list of initial {@link NotificationError} instances
     */
    private NotificationValidation(List<NotificationError> errors) {
        this.errors = errors;
    }

    /**
     * Creates a new, empty validation notification container.
     *
     * @return a new {@link NotificationValidation} instance initialized with an
     *         empty error list
     */
    public static NotificationValidation create() {
        return new NotificationValidation(new ArrayList<>());
    }

    /**
     * Appends a validation error to the current context.
     *
     * @param error the {@link NotificationError} to be accumulated
     * @return this {@link NotificationValidation} instance to allow for method
     *         chaining
     */
    public NotificationValidation append(NotificationError error) {
        errors.add(error);
        return this;
    }

    /**
     * Indicates whether any validation errors have been accumulated in this
     * context.
     *
     * @return {@code true} if the internal error list is not {@code null} and
     *         contains
     *         at least one error; {@code false} otherwise
     */
    public boolean hasErrors() {
        return this.errors != null && !this.errors.isEmpty();
    }
}