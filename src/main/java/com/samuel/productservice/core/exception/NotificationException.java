package com.samuel.productservice.core.exception;

import com.samuel.productservice.core.validation.NotificationValidation;

/**
 * Signals that a domain validation phase has failed with accumulated errors.
 * <p>
 * This exception encapsulates a {@link NotificationValidation} context to
 * transport multiple business rule violations up the execution stack.
 */
public class NotificationException extends DomainException {

    /**
     * Constructs a new exception with a context message and the accumulated
     * validation errors.
     *
     * @param message                the detailed error message explaining the
     *                               validation failure context
     * @param notificationValidation the validation container holding the collected
     *                               domain errors; must not be {@code null}
     */
    public NotificationException(
            final String message,
            final NotificationValidation notificationValidation) {
        super(message, notificationValidation.getErrors());
    }

    /**
     * Constructs a new exception with a context message and no associated
     * validation errors.
     *
     * @param message the detailed error message explaining the validation failure
     *                context
     */
    public NotificationException(String message) {
        super(message, null);
    }

}