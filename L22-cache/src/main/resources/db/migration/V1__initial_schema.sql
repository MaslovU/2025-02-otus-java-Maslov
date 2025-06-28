create sequence client_seq start with 1 increment by 1;
create sequence street_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;

create table address
(
    id bigint primary key,
    street varchar(200),
    client_id bigint unique
);

create table client
(
    id   bigint primary key,
    name varchar(50),
    address_id bigint,
    CONSTRAINT fk_address FOREIGN KEY(address_id) REFERENCES address(id)
);

create table phone
(
    id   bigint primary key,
    number varchar(50),
    client_id bigint,
    CONSTRAINT fk_client FOREIGN KEY(client_id) REFERENCES client(id)
);
