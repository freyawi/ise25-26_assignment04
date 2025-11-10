SET TIME ZONE 'UTC';

CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE product (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    name varchar(255) NOT NULL CHECK (name <> ''),
    description text,
    barcode varchar(255) UNIQUE,
    unit_price numeric(19,2) NOT NULL,
    tax_rate numeric(19,2),
    available boolean NOT NULL DEFAULT true,
    product_category varchar(255)
);
