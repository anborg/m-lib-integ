select
   c.id c_id,
   c.status c_status,
   c.type_id c_type_id,
   c.reportedby_person_id c_reportedby_person_id,
   c.createdby_emp_id c_createdby_emp_id,
   c.description c_description,
   c.address_id c_address_id,
   c.ts_create c_ts_create,
   c.ts_update c_ts_update,

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

   xc.id x_id,
   xc.xref_sys_id x_xref_sys_id,
   xc.xref_id x_xref_id,
   xc.ts_create x_ts_create,
   xc.ts_update x_ts_update,
   xc.ts_refreshed x_ts_refreshed

from
   INTEG_CASE c
   left join INTEG_CASE_ADDRESS addr on c.address_id = addr.id
   left join INTEG_XREF_CASE xc on c.id = xc.id
fetch first 20 rows only