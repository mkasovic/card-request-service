insert into consumer (
  id, username, password, role
) values (
  nextval('consumer_seq'), 'test', 'test', 'CONSUMER'
);

insert into card_request (
  id, first_name, last_name, oib, card_status
) values (
  nextval('card_request_seq'), 'MAX', 'MULE', '12345678901', 'APPROVED'
);
