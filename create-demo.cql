CREATE KEYSPACE demo WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

USE demo;

CREATE TABLE products (
  id int,
  code text,
  description text,
  disabled boolean,
  PRIMARY KEY ((id))
);