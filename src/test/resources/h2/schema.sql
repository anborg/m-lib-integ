CREATE SCHEMA IF NOT EXISTS INTEG;
set schema integ;
drop table if exists INTG_PERSON;
drop TABLE if exists integ.INTG_PERSON ;
CREATE TABLE integ.INTG_PERSON (
    id  SERIAL PRIMARY KEY
    , firstname VARCHAR
    , lastname VARCHAR
    , email VARCHAR(20) -- contact_channels.email
    , phone1 VARCHAR(15) -- contact_channels.phone1
    , phone2 VARCHAR(15)
    , address_id NUMERIC(10)
    , ts_create TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    , ts_update TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);


drop table if exists integ.INTG_ADDRESS;
CREATE TABLE integ.INTG_ADDRESS ( -- contact_channels.postalAddress
    id  SERIAL PRIMARY KEY
    , streetnum VARCHAR(10)
    , streetname VARCHAR(30)
    , city VARCHAR(20)
    , country VARCHAR(20)
    , postalcode VARCHAR(10)
    , lat DECIMAL(8,6)
    , lon DECIMAL(9,6)
    , ts_create TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    , ts_update TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);

insert into integ.INTG_ADDRESS (streetnum, streetname, city, country, postalcode)
values( '101', 'My Street', 'My City', 'Canada', 'L1L0Z0');
commit;
insert into integ.INTG_PERSON (id, firstname, lastname, email, phone1, phone2, address_id) values
(1, 'Jane', 'Doe', 'me@gmail.com', '1111111111', '2222222222',3)
;
commit;

