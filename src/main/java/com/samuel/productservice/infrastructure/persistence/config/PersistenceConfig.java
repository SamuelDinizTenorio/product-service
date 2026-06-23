package com.samuel.productservice.infrastructure.persistence.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.samuel.productservice.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.samuel.productservice.infrastructure.persistence.entity")
public class PersistenceConfig {

}
