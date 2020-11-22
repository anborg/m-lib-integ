package access.integ;

import access.amanda.AmandaUtil;
import access.hansen.HansenUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import integ.dao.jdbi.JdbiDbUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class IntegUtil {
    public static IntegService mock() {
        IntegService service = withDs2(inmemDS());
        SubsystemService amandaService = AmandaUtil.mock();
        SubsystemService hansenService = HansenUtil.mock();
        //TODO Be careful not to mix
        service.setSubsystemService(Subsys.AMANDA, amandaService);
        service.setSubsystemService(Subsys.HANSEN, hansenService);

        return service;
    }

    // Use for junit
    @Deprecated
    public static SubsystemService inMem() {
        return withDs(inmemDS());
    }

    // Use for integ-test
    public static IntegService dev() {
        return withDs2(devDS());
    }

    private static IntegService withDs2(DataSource ds) {
        IntegDao dao = JdbiDbUtil.getIntegDao(ds);
        return new IntegServiceImpl(dao);
    }

    @Deprecated
    private static SubsystemService withDs(DataSource ds) {
        CRUDDao<Model.Person> personDao = JdbiDbUtil.getDao(ds, Model.Person.class);
        CRUDDao<Model.Xref> xrefDao = JdbiDbUtil.getDao(ds, Model.Xref.class);
        CRUDDao<Model.PostalAddress> addressDao = JdbiDbUtil.getDao(ds, Model.PostalAddress.class);
        CRUDDao<Model.Case> caseDao = JdbiDbUtil.getDao(ds, Model.Case.class);
        return new IntegSubsystemServiceImpl(personDao, xrefDao, addressDao, caseDao);
    }

    private static String inmemDbUrl_anon_one_connection() {
        String INMEM_DB_URL = "jdbc:h2:mem:;" +
                "INIT=RUNSCRIPT FROM 'classpath:integ/schema.sql'\\;" +
                "RUNSCRIPT FROM 'classpath:integ/data.sql'";
        return INMEM_DB_URL;
    }

    final static Random random = new Random();

    private static DataSource devDS() {
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
        org.h2.jdbcx.JdbcDataSource ds ;
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
    public static Model.Person buildSubsystemPerson(Model.Xref xref, Model.Person ofPerson) {
        return Model.Person.newBuilder()
                .setId(xref.getXrefId())
                .setFirstName(ofPerson.getFirstName())
                .setLastName(ofPerson.getLastName())
                .setEmail(ofPerson.getEmail())
                .setPhone1(ofPerson.getPhone1())
                .setPhone2(ofPerson.getPhone2())
                .setAddress(ofPerson.getAddress())
                //.putXrefAccounts()//No xref
                .build();
    }

    private static class Dummy {/*for prop file load*/
    }

}
