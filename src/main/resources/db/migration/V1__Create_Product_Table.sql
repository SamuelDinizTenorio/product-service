CREATE TABLE product (
    id VARCHAR(36) NOT NULL,
    sku VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    stock DECIMAL(10, 2) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_sku UNIQUE (sku)
);
