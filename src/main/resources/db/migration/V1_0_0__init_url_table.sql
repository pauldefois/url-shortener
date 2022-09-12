create sequence url_seq_id start 1 increment 10;

create table url
(
    id                 int8 primary key,
    url                varchar(1000) not null,
    hash               varchar(8)    not null,
    creation_date_time timestamp     not null
);

create unique index uk_url on url (hash)