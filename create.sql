DROP TABLE stock;
DROP SEQUENCE stock_id_seq;

CREATE SEQUENCE stock_id_seq;
CREATE TABLE stock (
    id bigint NOT NULL DEFAULT nextval('stock_id_seq'),
    name text,
    value decimal(32, 2),
    currency text
);