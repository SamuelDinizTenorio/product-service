package com.samuel.productservice.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductEntity implements Persistable<UUID> {

    @Id
    @jakarta.persistence.Column(columnDefinition = "VARCHAR(36)")
    private UUID id;
    private String sku;
    private String name;
    private BigDecimal stock;
    private BigDecimal cost;
    private BigDecimal price;

    @Transient
    private boolean isNew = true;

    public static ProductEntity create(
            final UUID id,
            final String sku,
            final String name,
            final BigDecimal stock,
            final BigDecimal cost,
            final BigDecimal price,
            final boolean isNew) {
        return new ProductEntity(id, sku, name, stock, cost, price, isNew);
    }

    @PostLoad
    protected void voidPostLoad() {
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
