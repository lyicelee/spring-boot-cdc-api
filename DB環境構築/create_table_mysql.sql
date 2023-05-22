DROP TABLE cdc_api_db.category;
create table cdc_api_db.category (
  id BIGINT AUTO_INCREMENT not null
  , category_name VARCHAR(255) not null
  , primary key (id)
);

DROP TABLE cdc_api_db.product;
create table cdc_api_db.product (
  id BIGINT AUTO_INCREMENT not null
  , category_id BIGINT
  , product_name VARCHAR(255) not null
  , primary key (id)
);

DROP TABLE cdc_api_db.orders;
create table cdc_api_db.orders (
  id BIGINT AUTO_INCREMENT not null
  , product_id BIGINT
  , customer_name VARCHAR(255) not null
  , order_total numeric(10, 2) not null
  , primary key (id)
);

