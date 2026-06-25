package com.samuel.productservice.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuel.productservice.infrastructure.persistence.entity.ProductEntity;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

}
