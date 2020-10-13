package access.integ;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import integ.dao.jdbi.JdbiDbUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IntegUtil {

    public static final String sql_select_person = "select\n" +
            "    ip.\"id\",\n" +
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
            "    intg_person ip\n" +
            "    left join intg_address ia on ip.address_id = ia.id\n" +
            "where\n" +
            "    ip.id = :id";

    public static DataSource devDatasource() {
        final Properties props = new Properties();
        PGSimpleDataSource ds = null;
        try (final InputStream stream =
                     new Dummy().getClass().getClassLoader().getResourceAsStream("db-dev.properties")) {
            props.load(stream);
            ds = new PGSimpleDataSource();
            String host = props.getProperty("db.dev.host");
            String port = props.getProperty("db.dev.port");
            String dbname = props.getProperty("db.dev.dbname");
            String schemaname = props.getProperty("db.dev.schemaName");
            String user = props.getProperty("db.dev.user");
            String pwd = props.getProperty("db.dev.password");
            //
            ds.setServerName(host);
            ds.setDatabaseName(dbname);
            ds.setCurrentSchema(schemaname);
            ds.setUser(user);
            ds.setPassword(pwd);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HikariConfig hc = new HikariConfig();
        hc.setDataSource(ds);
        hc.setMaximumPoolSize(2);
        HikariDataSource hdc = new HikariDataSource(hc);
        return hdc;
    }

    private static class Dummy {

    }

    public static SubsystemService dev() {
        final var ds = devDatasource();
        CRUDDao<Model.Person> personDao = JdbiDbUtil.getDao(ds, Model.Person.class);
        CRUDDao<Model.Case> caseDao = JdbiDbUtil.getDao(ds, Model.Case.class);
        return new IntegServiceImpl(personDao, caseDao);
    }

}
/*

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
    left join intg_address ia on ip.address_id = ia.id
where
    ip.id = :id
;

*/
