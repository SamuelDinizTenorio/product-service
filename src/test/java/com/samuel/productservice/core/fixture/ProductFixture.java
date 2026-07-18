package com.samuel.productservice.core.fixture;

import java.math.BigDecimal;
import java.util.UUID;

import com.samuel.productservice.core.model.Product;
import com.samuel.productservice.core.model.Sku;

import lombok.experimental.UtilityClass;

/**
 * Test fixture factory providing pre-configured {@link Product} instances for
 * testing purposes.
 * <p>
 * This fixture simplifies test setup by offering standardized default datasets
 * and customization methods, minimizing test maintenance when the underlying
 * domain structure changes.
 */
@UtilityClass
public class ProductFixture {

    /**
     * Creates a standard {@link Product} instance populated with valid default
     * data.
     *
     * @return a new pre-configured {@link Product} instance with a default SKU
     */
    public Product any() {
        return Product.create(
                "SKU-DEFAULT",
                "Default Product Name",
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.valueOf(20));
    }

    /**
     * Reconstitutes an existing {@link Product} instance with a specified unique
     * identifier.
     * <p>
     * This method bypasses the domain validation rules applied during standard
     * creation,
     * simulating an aggregate loaded from a datastore.
     *
     * @param id the unique {@link UUID} of the product to reconstitute; 
     *           must not be {@code null}
     * @return a reconstituted {@link Product} instance containing the given
     *         identifier
     */
    public Product reconstituteWithId(final UUID id) {
        return Product.reconstitute(
                id,
                Sku.reconstitute("SKU-DEFAULT"),
                "Default Product Name",
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.valueOf(20));
    }

    /**
     * Reconstitutes an existing {@link Product} instance using a randomly generated
     * identifier.
     *
     * @return a reconstituted {@link Product} instance containing a randomized
     *         {@link UUID}
     */
    public Product anyReconstituted() {
        return reconstituteWithId(UUID.randomUUID());
    }

    /**
     * Creates a {@link Product} instance with a customized, collision-resistant SKU
     * pattern.
     * <p>
     * Appends a random four-character suffix to the provided base SKU to ensure
     * uniqueness and prevent database constraint violations during integration
     * tests. The base SKU is automatically truncated to 25 characters if necessary
     * to guarantee the final generated string strictly respects the domain's
     * 30-character maximum limit.
     *
     * @param baseSku the base string used to generate the unique stock keeping unit
     * @return a new {@link Product} instance configured with a unique SKU and
     *         matching name
     */
    public Product withSku(final String baseSku) {
        final var uniqueSuffix = UUID.randomUUID().toString().substring(0, 4);

        final var safeBase = baseSku.length() > 25 ? baseSku.substring(0, 25) : baseSku;
        final var generatedSku = safeBase + "-" + uniqueSuffix;

        return Product.create(
                generatedSku,
                "Product Test " + safeBase,
                BigDecimal.ONE,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(200));
    }
}
