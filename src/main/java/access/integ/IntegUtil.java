package access.integ;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import integ.jdbi.dao.JdbiDbUtil;
import integ.dao.CaseDao;
import integ.dao.PersonDao;
import mkm.service.SubsystemService;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IntegUtil {

    public static SubsystemService dev() {
        final var ds = devDatasource();
        PersonDao personDao = JdbiDbUtil.getDao(ds, PersonDao.class);
        CaseDao caseDao = JdbiDbUtil.getDao(ds, CaseDao.class);
        return new IntegServiceImpl(personDao, caseDao);
    }

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

}
