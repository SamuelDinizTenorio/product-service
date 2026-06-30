package com.samuel.productservice.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configures the persistence layer infrastructure for the product service.
 * <p>
 * This configuration class explicitly enables Spring Data JPA repositories and
 * defines the package boundaries for JPA entity scanning. It ensures that data
 * access adapters and relational entity mappings within the infrastructure
 * layer are correctly registered and managed by the Spring application context.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.samuel.productservice.infrastructure.adapter.persistence")
@EntityScan(basePackages = "com.samuel.productservice.infrastructure.adapter.persistence.entity")
public class PersistenceConfig {

}
