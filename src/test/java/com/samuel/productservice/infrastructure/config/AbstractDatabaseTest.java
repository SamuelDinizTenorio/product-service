package com.samuel.productservice.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract base class providing a singleton database container shared across test execution slices.
 * <p>
 * Ensures a single {@link MySQLContainer} instance is initialized and booted when the class is loaded,
 * allowing test classes extending this class to reuse the active database instance.
 */
public abstract class AbstractDatabaseTest {

    /**
     * Shared MySQL container instance configured with test database credentials and startup timeout.
     * <p>
     * Annotated with {@link ServiceConnection} to automatically establish dynamic database connections
     * for Spring Boot integration tests.
     */
    @ServiceConnection
    protected static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("product_db")
            .withUsername("test")
            .withPassword("test")
            .withStartupTimeout(Duration.ofMinutes(2));

    static {
        MY_SQL_CONTAINER.start(); // Starts the container once when the ClassLoader loads this class.
    }
}