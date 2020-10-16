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
    intg_person ip
    left join intg_address ia on ip.address_id = ia.id;



