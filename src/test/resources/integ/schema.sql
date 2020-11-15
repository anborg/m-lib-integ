--SET MODE ORACLE;
CREATE SCHEMA IF NOT EXISTS INTEG;
set schema integ;
drop table if exists INTG_PERSON;
drop TABLE if exists integ.INTEG_PERSON;

CREATE TABLE integ.INTEG_PERSON (
    id  SERIAL PRIMARY KEY
    , firstname VARCHAR(30)
    , lastname VARCHAR(30)
    , email VARCHAR(20) -- contact_channels.email
    , phone1 VARCHAR(15) -- contact_channels.phone1
    , phone2 VARCHAR(15)
    , address_id NUMERIC(10)
    , ts_create TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    , ts_update TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);


drop table if exists integ.INTEG_ADDRESS;
CREATE TABLE integ.INTEG_ADDRESS ( -- contact_channels.postalAddress
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

drop table if exists integ.INTEG_XREF_PERSON;
CREATE TABLE integ.INTEG_XREF_PERSON ( -- contact_channels.postalAddress
    --  put uniq constraint {id_person, xref_sysid} if one person can have only one account.
     id NUMERIC(10) -- points to INTEG_PERSON
    , xref_sys_id VARCHAR(10)
    , xref_person_id VARCHAR(30)
    , ts_create TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    , ts_update TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    , ts_ss_refreshed TIMESTAMP  without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
    ,PRIMARY KEY (id,xref_sys_id,xref_sys_id)
);
--insert into integ.INTEG_ADDRESS (streetnum, streetname, city, country, postalcode)
--values( '101', 'My Street', 'My City', 'Canada', 'L1L0Z0');
--commit;
--insert into integ.INTEG_PERSON (id, firstname, lastname, email, phone1, phone2, address_id) values
--(1, 'Jane', 'Doe', 'me@gmail.com', '1111111111', '2222222222',3)
--;
--commit;

