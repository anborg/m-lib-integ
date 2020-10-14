package access.integ;

public interface Queries {
    String sql_person_select_all = "select\n" +
            "    ip.id,\n" +
            "    ip.firstname,\n" +
            "    ip.lastname,\n" +
            "    ip.email,\n" +
            "    ip.phone1,\n" +
            "    ip.phone2,\n" +
            "    ip.address_id,\n" +
            "    ip.ts_create,\n" +
            "    ip.ts_update,\n" +
            "    --ia.\"id\",\n" +
            "    ia.streetname,\n" +
            "    ia.streetnum,\n" +
            "    ia.city,\n" +
            "    ia.country,\n" +
            "    ia.postalcode,\n" +
            "    ia.lat,\n" +
            "    ia.lon,\n" +
            "    ia.ts_create addr_ts_create,\n" +
            "    ia.ts_update addr_ts_update\n" +
            "from\n" +
            "    integ.INTEG_PERSON ip\n" +
            "    left join integ.integ_address ia on ip.address_id = ia.id\n" ;
    String sql_person_select_byId = sql_person_select_all+
            "where\n" +
            "    ip.id = :id";

    String sql_address_insert = "";
}
/* Person Select

select
    ip."id",
    ip.firstname,
    ip.lastname,
    ip.email,
    ip.phone1,
    ip.phone2,
    ip.address_id,
    ip.ts_create,
    ip.ts_update,
    --ia."id",
    ia.streetname,
    ia.streetnum,
    ia.city,
    ia.country,
    ia.postalcode,
    ia.lat,
    ia.lon,
    ia.ts_create addr_ts_create,
    ia.ts_update addr_ts_update
from
    integ_person ip
    left join integ_address ia on ip.address_id = ia.id
where
    ip.id = :id
;
*/


// Address ============================
/*
insert into integ_address
values (
    -- :id, --autogen
    :city,
    :country,
    :lat,
    :lon,
    :postalcode,
    :streetname,
    :streetnum
    )
;
*/
