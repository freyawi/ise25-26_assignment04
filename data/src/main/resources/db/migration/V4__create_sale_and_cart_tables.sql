SET TIME ZONE 'UTC';

CREATE SEQUENCE sale_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE sale (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    timestamp timestamp NOT NULL,
    total_price numeric(19,2) NOT NULL,
    status varchar(255) NOT NULL,
    cashier_id bigint NOT NULL REFERENCES cashier_account(id),
    pos_id bigint NOT NULL REFERENCES pos(id)
);

CREATE SEQUENCE cart_item_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE cart_item (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    quantity int NOT NULL CHECK (quantity > 0),
    unit_price numeric(19,2) NOT NULL,
    product_id bigint NOT NULL REFERENCES product(id),
    sale_id bigint NOT NULL REFERENCES sale(id)
);
