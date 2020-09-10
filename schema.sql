DROP TABLE historical;
DROP SEQUENCE historical_id_seq;
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

CREATE SEQUENCE historical_id_seq START 1;
CREATE TABLE historical (
    id bigint NOT NULL DEFAULT nextval('historical_id_seq'),
    ets_id bigint REFERENCES exchangetostock(id) ON DELETE CASCADE,
    value decimal(32, 2),
    volume bigint,
    date timestamp,
    PRIMARY KEY(id)
);


--INSERT INTO stock(name, symbol, value, currency) VALUES ('CDPROJEKT', 'CDR.XWAR', 320.48, 'EUR');
--INSERT INTO stock(name, symbol, value, currency) VALUES ('11BIT', '11B.XWAR', 507.00, 'EUR');
--INSERT INTO stock(name, symbol, value, currency) VALUES ('Apple', 'AAPL', 461.90, 'USD');

--INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1322, 2);
--INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1322, 3);
--INSERT INTO exchangetostock(exchange_id, stock_id) VALUES (1351, 4);

--INSERT INTO exchange(symbol, currency, name) VALUES ('XWAR', 'EUR', 'Warsaw Stock Exchange');
--INSERT INTO exchange(symbol, currency, name) VALUES ('XNAS', 'USD', 'NASDAQ Stock Exchange');

--INSERT INTO cachecontrol(endpoint_name, last_access) VALUES ('exchanges', '2020-07-31 16:15:14');
--INSERT INTO cachecontrol(endpoint_name, last_access) VALUES ('stocks.ARCX', '2020-07-31 17:15:14');

--INSERT INTO historical(ets_id, value, volume, date) VALUES (1322, 320.48, 5930, '2020-09-04 09:35:53');
--INSERT INTO historical(ets_id, value, volume, date) VALUES (1351, 461.90, 238312, '2020-09-04 09:36:12');

--update cachecontrol set last_access = '2020-07-29 16:48:00' where endpoint_name = 'exchanges';
SET CLIENT_ENCODING TO 'UTF-8';

--SELECT ets.id, e.name, s.name, s.symbol, s.value, s.currency
--FROM exchangetostock ets, exchange e, stock s
--WHERE ets.exchange_id = e.id AND ets.stock_id = s.id;