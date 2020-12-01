

CREATE TABLE integ.INTEG_ADDRESS (
    id  SERIAL PRIMARY KEY
    , streetnum VARCHAR(10)
    , streetname VARCHAR(30)
    , city VARCHAR(20)
    , country VARCHAR(20)
    , postalcode VARCHAR(10)
    , lat DECIMAL(8,6)
    , lon DECIMAL(9,6)
    , ts_create TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
    , ts_update TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE integ.INTEG_XREF_PERSON (
     id SERIAL PRIMARY KEY
    , xref_sys_id VARCHAR(10)
    , xref_person_id VARCHAR(30)
    , xref_status VARCHAR(10)
    , ts_create TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
    , ts_update TIMESTAMP  DEFAULT CURRENT_TIMESTAMP -- ON UPDATE CURRENT_TIMESTAMP
    , ts_refreshed TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ,PRIMARY KEY (id,xref_sys_id)
)
;