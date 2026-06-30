package com.samuel.productservice.infrastructure.adapter.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuel.productservice.infrastructure.adapter.persistence.entity.ProductEntity;

/**
 * Provides standard data access operations for {@link ProductEntity}
 * persistence records using Spring Data JPA.
 * <p>
 * This interface abstracts relational database interactions, leveraging Spring
 * Data's runtime infrastructure to fulfill CRUD, execution optimization, and
 * query generation contract requirements.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

}
