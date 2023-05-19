create table category (
  id NUMBER(10) not null
  , category_name VARCHAR2(255) not null
  , primary key (id)
);

create table product (
  id NUMBER(10) not null
  , category_id NUMBER(10) not null
  , product_name VARCHAR2(255) not null
  , primary key (id)
);

create table orders (
  id NUMBER(10) not null
  , product_id NUMBER(10) not null
  , customer_name VARCHAR2(255) not null
  , order_total NUMBER(10, 2) not null
  , primary key (id)
);

