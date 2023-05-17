create table category (
  id serial not null
  , category_name VARCHAR(255) not null
  , primary key (id)
);

create table product (
  id serial not null
  , category_id bigint
  , product_name VARCHAR(255) not null
  , primary key (id)
);

create table orders (
  id serial not null
  , product_id bigint
  , customer_name VARCHAR(255) not null
  , order_total numeric(10, 2) not null
  , primary key (id)
);

