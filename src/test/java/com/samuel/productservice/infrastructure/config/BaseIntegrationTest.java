package com.samuel.productservice.infrastructure.config;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Abstract base class for containerized integration testing using {@link SpringBootTest}.
 * <p>
 * Boots the Spring application context wrapped with a shared, managed test
 * database instance to ensure testing isolation and environment reliability.
 */
@SpringBootTest
public abstract class BaseIntegrationTest extends AbstractDatabaseTest {
}