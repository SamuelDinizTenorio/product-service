package com.samuel.productservice.infrastructure.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import java.time.Duration;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract base class for containerized integration testing.
 * <p>
 * Boots the Spring application context wrapped with a shared, managed test
 * database instance to ensure testing isolation and environment reliability.
 */
@SpringBootTest
public abstract class BaseIntegrationTest {

    /**
     * Managed container runtime instance executing a isolated MySQL database
     * server.
     * <p>
     * Bound automatically to the Spring context connection factory via
     * {@link ServiceConnection}.
     */
    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("product_db")
            .withUsername("test")
            .withPassword("test")
            .withStartupTimeout(Duration.ofMinutes(2));

    static {
        mySQLContainer.start();
    }
}