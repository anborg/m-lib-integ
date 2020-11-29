SET MODE ORACLE;
CREATE SCHEMA IF NOT EXISTS INTEG;
set schema integ;
drop TABLE if exists integ.INTEG_PERSON;

CREATE TABLE INTEG_PERSON (
    id  INTEGER  GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY
    , firstname VARCHAR(30)
    , lastname VARCHAR(30)
    , email VARCHAR(20)
    , phone1 VARCHAR(15)
    , phone2 VARCHAR(15)
    , address_id NUMERIC(10)
    , ts_create TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
    , ts_update TIMESTAMP  DEFAULT CURRENT_TIMESTAMP -- ON UPDATE CURRENT_TIMESTAMP
);


drop table if exists integ.INTEG_ADDRESS;
CREATE TABLE INTEG_ADDRESS (
    id  INTEGER  GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY
    , streetnum VARCHAR(10)
    , streetname VARCHAR(30)
    , city VARCHAR(20)
    , country VARCHAR(20)
    , postalcode VARCHAR(10)
    , lat DECIMAL(8,6)
    , lon DECIMAL(9,6)
    , ts_create TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
    , ts_update TIMESTAMP  DEFAULT CURRENT_TIMESTAMP -- ON UPDATE CURRENT_TIMESTAMP
);

drop table if exists integ.INTEG_XREF_PERSON;
CREATE TABLE INTEG_XREF_PERSON ( -- contact_channels.postalAddress
    --  put uniq constraint {id_person, xref_sysid} if one person can have only one account.
     id NUMBER(5)  NOT NULL
    , xref_sys_id VARCHAR(10) NOT NULL
    , xref_person_id VARCHAR(30)
    , xref_status VARCHAR(10)
    , ts_create TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
    , ts_update TIMESTAMP  DEFAULT CURRENT_TIMESTAMP -- ON UPDATE CURRENT_TIMESTAMP -- ON UPDATE won't work for postgres https://stackoverflow.com/questions/9556474/how-do-i-automatically-update-a-timestamp-in-postgresql/9556527
    , ts_refreshed TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ,PRIMARY KEY (id,xref_sys_id)
)
;

insert into INTEG_ADDRESS (id, streetnum, streetname, city, country, postalcode) values
( 1,'101', 'My Street', 'My City', 'Canada', 'L1L0Z0')
,( 2,'202', 'My Street', 'My City', 'Canada', 'L1L0Z0')
,( 3,'303', 'My Street', 'My City', 'Canada', 'L1L0Z0')
;


insert into INTEG_PERSON (id, firstname, lastname, email, phone1, phone2, address_id) values
(1, 'Miki', 'Mouse', 'miki.mouse@gmail.com', '1111111111', '999999999',1)
,(2, 'Rat', 'Atouee', 'ratatouee@gmail.com', '2222222222', '999999999',2)
,(3, 'Richard', 'Seters', 'rseters@gmail.com', '3333333333', '999999999',3)
;

insert into INTEG_XREF_PERSON (id, xref_sys_id, xref_person_id) values
( 1,'AMANDA', null)
,( 2,'HANSEN', null)
,( 3,'AMANDA', null)
,( 3,'HANSEN', null)
;


commit;
