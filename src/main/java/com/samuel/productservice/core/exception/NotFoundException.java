package com.samuel.productservice.core.exception;

import java.util.ArrayList;
import java.util.List;

import com.samuel.productservice.core.validation.NotificationError;

/**
 * Exception thrown when a requested domain resource cannot be found.
 * <p>
 * This exception encapsulates specific validation or business errors that
 * triggered the resource discovery failure.
 */
public class NotFoundException extends DomainException {

    /**
     * Constructs a new exception with the specified detail message and errors.
     *
     * @param message the detail message explaining the cause of the exception
     * @param errors  the list of {@link NotificationError} details associated with
     *                this failure
     */
    public NotFoundException(
            String message,
            List<NotificationError> errors) {
        super(message, errors);
    }

    /**
     * Creates a new instance of this exception containing an empty list of errors.
     *
     * @param message the detail message explaining the cause of the exception
     * @return a new instance of {@link NotFoundException}
     */
    public static NotFoundException create(String message) {
        return new NotFoundException(message, new ArrayList<>());
    }
}
