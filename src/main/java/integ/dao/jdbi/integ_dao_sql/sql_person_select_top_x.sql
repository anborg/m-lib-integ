select
   p.id p_id,
   p.firstname p_firstname,
   p.lastname p_lastname,
   p.email p_email,
   p.phone1 p_phone1,
   p.phone2 p_phone2,
   --p.address_id,
   p.ts_create p_ts_create,
   p.ts_update p_ts_update,

   addr.id a_id,
   addr.streetname a_streetname,
   addr.streetnum a_streetnum,
   addr.city a_city,
   addr.country a_country,
   addr.postalcode a_postalcode,
   addr.lat a_lat,
   addr.lon a_lon,
   addr.ts_create a_ts_create,
   addr.ts_update a_ts_update,

   xp.id x_id,
   xp.xref_sys_id x_xref_sys_id,
   xp.xref_person_id x_xref_person_id,
   xp.ts_create x_ts_create,
   xp.ts_update x_ts_update,
   xp.ts_refreshed x_ts_refreshed

from
   INTEG_PERSON p
   left join INTEG_ADDRESS addr on p.address_id = addr.id
   left join INTEG_XREF_PERSON xp on p.id = xp.id
--fetch first 20 rows only