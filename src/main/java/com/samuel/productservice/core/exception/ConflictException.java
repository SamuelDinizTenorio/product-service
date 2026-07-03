package com.samuel.productservice.core.exception;

import java.util.ArrayList;
import java.util.List;

import com.samuel.productservice.core.validation.NotificationError;

/**
 * Exception thrown when a business rule is violated due to a state conflict,
 * such as attempting to create a resource with an identifier that already
 * exists.
 */
public class ConflictException extends DomainException {

    /**
     * Constructs a new exception with the specified detail message and errors.
     *
     * @param message the detail message explaining the cause of the conflict
     * @param errors  the list of {@link NotificationError} details associated with
     *                this failure
     */
    public ConflictException(final String message, final List<NotificationError> errors) {
        super(message, errors);
    }

    /**
     * Creates a new instance of this exception containing an empty list of errors.
     *
     * @param message the detail message explaining the cause of the conflict
     * @return a new instance of {@link ConflictException}
     */
    public static ConflictException create(final String message) {
        return new ConflictException(message, new ArrayList<>());
    }
}