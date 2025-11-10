SET TIME ZONE 'UTC';

CREATE SEQUENCE refund_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE refund (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    timestamp timestamp NOT NULL,
    refund_amount numeric(19,2) NOT NULL,
    reason varchar(255),
    cashier_id bigint NOT NULL REFERENCES cashier_account(id),
    sale_id bigint NOT NULL REFERENCES sale(id)
);
