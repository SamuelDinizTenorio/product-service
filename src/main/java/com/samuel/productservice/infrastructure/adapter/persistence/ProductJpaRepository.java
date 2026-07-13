package com.samuel.productservice.infrastructure.adapter.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    /**
     * Executes a derived query to locate a specific product record by its unique
     * stock keeping unit.
     *
     * @param sku the {@link String} representing the stock keeping unit to match;
     *            must not be {@code null}
     * @return an {@link Optional} containing the found {@link ProductEntity}
     *         instance, or an empty {@link Optional} if no record matches the
     *         specified SKU
     */
    Optional<ProductEntity> findBySku(String sku);

    /**
     * Retrieves a paginated collection of all product records from the database.
     *
     * @param pageable the pagination and sorting metadata configuration;
     *                 must not be {@code null}
     * @return a {@link Page} containing the retrieved {@link ProductEntity}
     *         instances matching the pagination constraints
     */
    Page<ProductEntity> findAll(Pageable pageable);
}
