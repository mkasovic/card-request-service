create table card_request (
  id            bigint      not null constraint card_request_pkey primary key,
  first_name    varchar(25) not null,
  last_name     varchar(25) not null,
  oib           varchar(11) not null constraint card_request_oib unique,
  card_status   varchar(25) not null default 'REQUESTED'
);

create table consumer (
  id            bigint not null constraint consumer_pkey primary key,
  username      varchar(25) not null,
  password      varchar(25) not null,
  role          varchar(25) not null
);

create sequence card_request_seq start with 1 increment by 50;
create sequence consumer_seq     start with 1 increment by 50;
