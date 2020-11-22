package integ.dao.jdbi;

public interface Queries {
    String sql_person_select_all = "select\n" +
            "    p.id p_id,\n" +
            "    p.firstname p_firstname,\n" +
            "    p.lastname p_lastname,\n" +
            "    p.email p_email,\n" +
            "    p.phone1 p_phone1,\n" +
            "    p.phone2 p_phone2,\n" +
            "    --p.address_id,\n" +
            "    p.ts_create p_ts_create,\n" +
            "    p.ts_update p_ts_update,\n" +

            "    addr.id a_id,\n" +
            "    addr.streetname a_streetname,\n" +
            "    addr.streetnum a_streetnum,\n" +
            "    addr.city a_city,\n" +
            "    addr.country a_country,\n" +
            "    addr.postalcode a_postalcode,\n" +
            "    addr.lat a_lat,\n" +
            "    addr.lon a_lon,\n" +
            "    addr.ts_create a_ts_create,\n" +
            "    addr.ts_update a_ts_update,\n" +

            "    xp.id x_id,\n" +
            "    xp.xref_sys_id x_xref_sys_id,\n" +
            "    xp.xref_person_id x_xref_person_id,\n" +
            "    xp.ts_create x_ts_create,\n" +
            "    xp.ts_update x_ts_update,\n" +
            "    xp.ts_refreshed x_ts_refreshed\n" +

            "from\n" +
            "    integ.INTEG_PERSON p\n" +
            "    left join integ.integ_ADDRESS addr on p.address_id = addr.id\n" +
            "    left join integ.integ_XREF_PERSON xp on p.id = xp.id\n" ;
    String sql_person_select_byId = sql_person_select_all+
            "where\n" +
            "    p.id = :id";

    String sql_address_insert = "";
}
/* Person Select

select
    p."id",
    p.firstname,
    p.lastname,
    p.email,
    p.phone1,
    p.phone2,
    p.address_id,
    p.ts_create,
    p.ts_update,
    --addr."id",
    addr.streetname,
    addr.streetnum,
    addr.city,
    addr.country,
    addr.postalcode,
    addr.lat,
    addr.lon,
    addr.ts_create addr_ts_create,
    addr.ts_update addr_ts_update
from
    integ_person p
    left join integ_address addr on p.address_id = addr.id
where
    p.id = :id
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
