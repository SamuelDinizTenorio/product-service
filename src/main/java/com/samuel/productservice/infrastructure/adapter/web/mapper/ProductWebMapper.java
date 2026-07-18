package com.samuel.productservice.infrastructure.adapter.web.mapper;

import static java.util.Objects.requireNonNull;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.infrastructure.adapter.web.dto.ProductRequest;
import com.samuel.productservice.infrastructure.adapter.web.dto.ProductResponse;

/**
 * Mapper utility responsible for translating between web data transfer objects and domain models.
 * <p>
 * This component decouples the web presentation layer from the core domain logic, 
 * facilitating clean data mapping boundaries for product transformations.
 */
public class ProductWebMapper {

    /**
     * Maps a {@link ProductRequest} data transfer object to a core {@link Product} domain entity.
     *
     * @param productRequest the incoming web request containing product payload attributes; must not be {@code null}
     * @return a new {@link Product} domain instance initialized with the requested attributes
     */
    public Product toDomain(ProductRequest productRequest) {
        requireNonNull(productRequest, "productRequest cannot be null");

        return Product.create(
                productRequest.sku(),
                productRequest.name(),
                productRequest.stock(),
                productRequest.cost(),
                productRequest.price()
        );
    }

    /**
     * Maps a {@link Product} domain entity to a {@link ProductResponse} data transfer object.
     *
     * @param product the core domain model instance representing the product state; must not be {@code null}
     * @return a new {@link ProductResponse} encapsulating the flattened representation of the product resource
     */
    public ProductResponse toResponse(Product product) {
        requireNonNull(product, "product cannot be null");

        return new ProductResponse(
                product.getId(),
                product.getSku().getValue(),
                product.getName(),
                product.getStock(),
                product.getCost(),
                product.getPrice()
        );
    }
}
