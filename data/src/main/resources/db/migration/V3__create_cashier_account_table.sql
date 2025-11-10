SET TIME ZONE 'UTC';

CREATE SEQUENCE cashier_account_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE cashier_account (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    username varchar(255) NOT NULL UNIQUE CHECK (username <> ''),
    password varchar(255) NOT NULL CHECK (password <> ''),
    first_name varchar(255) NOT NULL CHECK (first_name <> ''),
    last_name varchar(255) NOT NULL CHECK (last_name <> ''),
    email varchar(255),
    is_active boolean NOT NULL DEFAULT true,
    pos_id bigint NOT NULL REFERENCES pos(id)
);
