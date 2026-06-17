package com.samuel.productservice.core.exception;

import java.util.List;

import com.samuel.productservice.core.validation.NotificationError;

import lombok.Getter;

/**
 * Serves as the base class for all business domain exceptions within the
 * application.
 * <p>
 * This exception extends {@link RuntimeException} and acts as a transport
 * mechanism for a list of {@link NotificationError} objects, allowing
 * multiple domain validation failures to be processed together.
 */
@Getter
public class DomainException extends RuntimeException {

    /**
     * The collection of specific domain validation errors associated with this
     * exception.
     */
    private List<NotificationError> errors;

    /**
     * Constructs a new domain exception with the specified detail message and
     * accumulated errors.
     *
     * @param message the detailed error message explaining the cause of the
     *                business failure
     * @param errors  the list of {@link NotificationError} instances representing
     *                the specific validation rule violations; can be {@code null}
     *                if no granular error details are available
     */
    public DomainException(
            final String message,
            final List<NotificationError> errors) {
        super(message);
        this.errors = errors;
    }
}