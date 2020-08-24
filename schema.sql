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
    symbol text,
    value decimal(32, 2),
    currency text,
    PRIMARY KEY(id)
);

CREATE SEQUENCE exchange_id_seq START 1;
CREATE TABLE exchange (
    id bigint NOT NULL DEFAULT nextval('exchange_id_seq'),
    symbol text UNIQUE,
    name text,
    currency text,
    PRIMARY KEY(id)
);

CREATE SEQUENCE exchangetostock_id_seq START 1;
CREATE TABLE exchangetostock (
    id bigint NOT NULL DEFAULT nextval('exchangetostock_id_seq'),
    exchange_id bigint REFERENCES exchange(id),
    stock_id bigint REFERENCES stock(id) ON DELETE CASCADE,
    PRIMARY KEY(id)
);

CREATE SEQUENCE cachecontrol_id_seq START 1;
CREATE TABLE cachecontrol (
    id bigint NOT NULL DEFAULT nextval('cachecontrol_id_seq'),
    endpoint_name text UNIQUE,
    last_access timestamp,
    PRIMARY KEY(id)
);


INSERT INTO stock(name, symbol, value, currency) VALUES ('CD Projekt', 'CDR', 320.48, 'PLN');
INSERT INTO stock(name, symbol, value, currency) VALUES ('11 bit studios', '11B', 507.00, 'PLN');
INSERT INTO stock(name, symbol, value, currency) VALUES ('Apple', 'AAPL', 461.90, 'USD');

INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1322, 2);
INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1322, 3);
INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1351, 4);

INSERT INTO exchange(symbol, currency, name) VALUES ('WA', 'PLN', 'WARSAW STOCK EXCHANGE/EQUITIES/MAIN MARKET');
INSERT INTO exchange(symbol, currency, name) VALUES ('BK', 'THB', 'STOCK EXCHANGE OF THAILAND');
INSERT INTO exchange(symbol, currency, name) VALUES ('KS', 'KRW', 'KOREA EXCHANGE (STOCK MARKET)');

INSERT INTO cachecontrol(endpoint_name, last_access) VALUES ('exchanges', '2020-07-31 16:15:14');

update cachecontrol set last_access = '2020-07-29 16:48:00' where endpoint_name = 'exchanges';
SET CLIENT_ENCODING TO 'UTF-8';

SELECT ets.id, e.name, s.name, s.symbol, s.value, s.currency
FROM exchangetostock ets, exchange e, stock s
WHERE ets.exchange_id = e.id AND ets.stock_id = s.id;