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
import java.util.Random;

public class IntegUtil {
//    public static final String INMEM_DB_URL = "jdbc:h2:mem:test;" +
//            "INIT=RUNSCRIPT FROM 'classpath:h2/schema.sql'\\;" +
//            "RUNSCRIPT FROM 'classpath:h2/data.sql'";


    public static String inmemDbUrl_anon_one_connection(){
        String INMEM_DB_URL = "jdbc:h2:mem:;"+
        "INIT=RUNSCRIPT FROM 'classpath:integ/schema.sql'\\;" +
                "RUNSCRIPT FROM 'classpath:integ/data.sql'";


        return INMEM_DB_URL;
    }

    final static Random random = new Random();

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

    public static DataSource inmemDS() {
        final Properties props = new Properties();
        org.h2.jdbcx.JdbcDataSource ds = null;
        ds = new org.h2.jdbcx.JdbcDataSource();
        ds.setURL(inmemDbUrl_anon_one_connection());
        ds.setUser("sa");
        ds.setPassword("");
        HikariConfig hc = new HikariConfig();
        hc.setDataSource(ds);
        hc.setMaximumPoolSize(2);
        HikariDataSource hdc = new HikariDataSource(hc);
        return hdc;
    }

    public static SubsystemService inMem() {
        final var ds = inmemDS();
        return withDs(ds);
    }

    public static SubsystemService dev() {
        final var ds = devDatasource();
        return withDs(ds);

    }
    public static IntegServiceImpl withDs(DataSource ds){
        CRUDDao<Model.Person> personDao = JdbiDbUtil.getDao(ds, Model.Person.class);
        CRUDDao<Model.PostalAddress> addressDao = JdbiDbUtil.getDao(ds, Model.PostalAddress.class);
        CRUDDao<Model.Case> caseDao = JdbiDbUtil.getDao(ds, Model.Case.class);
        return new IntegServiceImpl(personDao, addressDao,caseDao);
    }


    private static class Dummy {/*for prop file load*/
    }

}
