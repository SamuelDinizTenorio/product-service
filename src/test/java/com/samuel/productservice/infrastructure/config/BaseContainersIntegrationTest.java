package com.samuel.productservice.infrastructure.config;

import java.time.Duration;

import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Base abstraction for integration tests requiring a localized database
 * environment.
 * <p>
 * This class provisions an isolated MySQL instance using Testcontainers for
 * data-layer testing.
 * It disables Spring Boot's default test database replacement behavior to
 * guarantee that repository operations interact directly with the containerized
 * environment.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Timeout(value = 10, unit = java.util.concurrent.TimeUnit.SECONDS)
public abstract class BaseContainersIntegrationTest {

    /**
     * The containerized MySQL database instance used across integration tests.
     * <p>
     * Annotated with {@link ServiceConnection} to dynamically discover and inject
     * connection properties into the Spring application context. This instance
     * targets the {@code mysql:8.0} image and is configured with a mandatory
     * startup timeout of two minutes.
     */
    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("product_db")
            .withUsername("test")
            .withPassword("test")
            .withStartupTimeout(Duration.ofMinutes(2));

    static {
        mySQLContainer.start(); // Starts the container once when the class is loaded.
    }
}
