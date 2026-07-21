package com.samuel.productservice.infrastructure.config;

import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Base abstraction for data-layer integration tests utilizing {@link DataJpaTest}.
 * <p>
 * Inherits the shared MySQL container instance from {@link AbstractDatabaseTest}.
 * Disables Spring Boot's default test database replacement behavior to
 * guarantee that repository operations interact directly with the containerized environment.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Timeout(value = 10, unit = java.util.concurrent.TimeUnit.SECONDS)
public abstract class BaseContainersIntegrationTest extends AbstractDatabaseTest {
}
