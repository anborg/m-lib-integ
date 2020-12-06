package integ.dao.jdbi;

import org.jdbi.v3.core.locator.ClasspathSqlLocator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Queries {
    //    public static final String sql_person_select_by_personId = ClasspathSqlLocator.findSqlOnClasspath("sql_person_select_by_personId");
    public static final String sql_person_select_top_x = ClasspathSqlLocator.findSqlOnClasspath(SqlFile.sql_person_select_top_x.toString());
    private static final Map<SqlFile, String> sql = null;

    public static String getSql(SqlFile sqlfile) {
        if (Objects.isNull(sql)) loadSqlFromReourceFiles();
        return sql.get(sqlfile);
    }

    private static void loadSqlFromReourceFiles() {
        var myEnum = List.of(SqlFile.values());
        ClassLoader classLoader = ClassLoader.getPlatformClassLoader();
        for (SqlFile sqlfile : SqlFile.values()) {
            String resourceFileName = "integ_dao_sql/" + sqlfile.toString() + ".sql";
            File file = new File(classLoader.getResource(resourceFileName).getFile());
            if (file.exists()) {
                try {
                    //Read File Content
                    String content = content = new String(Files.readAllBytes(file.toPath()));
                    sql.put(sqlfile, content);
                } catch (IOException e) {
                    throw new RuntimeException("INIT error: Error load sql file from : " + resourceFileName);
                }
            } else {
                throw new RuntimeException("INIT error: Error load sql file from : " + resourceFileName + ". Hint: Check if sql files exist for all Queries.SqlFile enums");
            }

        }
    }

    enum SqlFile {
        sql_person_select_by_personId,
        sql_person_select_top_x
    }

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
            "    INTEG_PERSON p\n" +
            "    left join INTEG_ADDRESS addr on p.address_id = addr.id\n" +
            "    left join INTEG_XREF_PERSON xp on p.id = xp.id\n" ;
    String sql_person_select_byId = sql_person_select_all+
            "where\n" +
            "    p.id = cast( :id as INTEGER )";

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

/*
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
   left join INTEG_XREF_PERSON xp on p.id = xp.id\n

 */



