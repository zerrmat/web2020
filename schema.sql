DROP TABLE cachecontrol;
DROP SEQUENCE cachecontrol_id_seq;
DROP TABLE exchangetostock;
DROP SEQUENCE exchangetostock_id_seq;
DROP TABLE exchange;
DROP SEQUENCE exchange_id_seq;
DROP TABLE stock;
DROP SEQUENCE stock_id_seq;

CREATE SEQUENCE stock_id_seq START 1;
CREATE TABLE stock (
    id bigint NOT NULL DEFAULT nextval('stock_id_seq'),
    name text,
    value decimal(32, 2),
    currency text,
    PRIMARY KEY(id)
);

CREATE SEQUENCE exchange_id_seq START 1;
CREATE TABLE exchange (
    id bigint NOT NULL DEFAULT nextval('exchange_id_seq'),
    code text UNIQUE,
    currency text,
    name text,
    PRIMARY KEY(id)
);

CREATE SEQUENCE exchangetostock_id_seq START 1;
CREATE TABLE exchangetostock (
    id bigint NOT NULL DEFAULT nextval('exchangetostock_id_seq'),
    exchange_id bigint REFERENCES exchange(id),
    stock_id bigint REFERENCES stock(id),
    PRIMARY KEY(id)
);

CREATE SEQUENCE cachecontrol_id_seq START 1;
CREATE TABLE cachecontrol (
    id bigint NOT NULL DEFAULT nextval('cachecontrol_id_seq'),
    endpoint_name text UNIQUE,
    last_access timestamp,
    PRIMARY KEY(id)
);


INSERT INTO stock(name, value, currency) VALUES ('CDR', 320.48, 'PLN');

INSERT INTO exchange(code, currency, name) VALUES ('WA', 'PLN', 'WARSAW STOCK EXCHANGE/EQUITIES/MAIN MARKET');
INSERT INTO exchange(code, currency, name) VALUES ('BK', 'THB', 'STOCK EXCHANGE OF THAILAND');
INSERT INTO exchange(code, currency, name) VALUES ('KS', 'KRW', 'KOREA EXCHANGE (STOCK MARKET)');

INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1, 1);

INSERT INTO cachecontrol(endpoint_name, last_access) VALUES ('exchanges', '2020-07-31 16:15:14');

update cachecontrol set last_access = '2020-07-29 16:48:00' where endpoint_name = 'exchanges';