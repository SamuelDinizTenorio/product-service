package com.samuel.productservice.infrastructure.adapter.web.fixture;

import java.math.BigDecimal;

import com.samuel.productservice.infrastructure.adapter.web.dto.ProductRequest;

import lombok.experimental.UtilityClass;

/**
 * Test fixture factory providing pre-configured {@link ProductRequest} instances
 * for testing purposes.
 * <p>
 * This fixture simplifies test setup by offering standardized default datasets
 * and customization methods, minimizing test maintenance when the underlying
 * web request data transfer object structure changes.
 */
@UtilityClass
public class ProductRequestFixture {

    /**
     * Creates a standard {@link ProductRequest} instance populated with valid
     * default payload data.
     *
     * @return a new pre-configured {@link ProductRequest} instance representing
     *         a valid web request
     */
    public static ProductRequest any() {
        return new ProductRequest(
                "SKU-123",
                "Mouse",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(120.00)
        );
    }

    /**
     * Creates a {@link ProductRequest} instance with a customized Stock Keeping Unit
     * identifier while maintaining all other attributes valid.
     * <p>
     * This utility allows isolated testing of payload validation boundaries specifically
     * targeted at the SKU field, ensuring that failures are caused strictly by the 
     * tested boundary condition.
     *
     * @param invalidSku the raw SKU string value to inject into the request payload
     * @return a {@link ProductRequest} configured with the supplied SKU value and
     *         otherwise valid default attributes
     */
    public static ProductRequest withInvalidSku(String invalidSku) {
        return new ProductRequest(
                invalidSku,
                "Mouse",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(120.00)
        );
    }
}
