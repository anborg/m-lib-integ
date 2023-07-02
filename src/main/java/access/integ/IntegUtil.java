package access.integ;

import access.amanda.AmandaUtil;
import access.hansen.HansenUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import integ.dao.jdbi.JdbiDbUtil;
import muni.model.Model;
import muni.service.SubsystemService;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class IntegUtil {
    /**
     * Build IntegService for a supplied datasource.
     *
     * @return
     */
    public static IntegService forDS(DataSource datasource) {
        IntegDao dao = JdbiDbUtil.getIntegDao(datasource);
        IntegService service = new IntegServiceImpl(dao);
        setSubsystem(service);
        return service;
    }

    private static void setSubsystem(IntegService service){
        SubsystemService amandaService = AmandaUtil.mock();
        SubsystemService hansenService = HansenUtil.mock();
        //TODO Be careful not to mix
        service.setSubsystemService(Subsys.AMANDA, amandaService);
        service.setSubsystemService(Subsys.HANSEN, hansenService);
    }


    /**
     * Build IntegService with a mock db, for testing
     * @return
     */
    public static IntegService mock() {return forDS(inmemDS());}

    public static IntegService postgres() {return forDS(pgDS());}

    public static IntegService oracle() {return forDS(oracleDS()); }

    private static String datasourceH2() {
        String INMEM_DB_URL = "jdbc:h2:mem:;" +
                "INIT=RUNSCRIPT FROM 'classpath:integ/schema.sql'\\;" +
                "RUNSCRIPT FROM 'classpath:integ/data.sql'";

        String INMEM_REACTIVE_DB_URL = "r2dbc:h2:mem:;" +
                "INIT=RUNSCRIPT FROM 'classpath:integ/schema.sql'\\;" +
                "RUNSCRIPT FROM 'classpath:integ/data.sql'";
        return INMEM_DB_URL;


    }

    final static Random random = new Random();


    /**
     * Build a pg ds from
     * @return
     */
    private static DataSource oracleDS() {
        final Properties props = new Properties();
        final HikariDataSource ds = new HikariDataSource();
        final String CONFIG_FILE = "ds-oracle.properties";
        String jdbcURL= null;
        try (final InputStream stream =
                     new Dummy().getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            props.load(stream);
            String host = props.getProperty("db.host");
            Integer port = Integer.valueOf(props.getProperty("db.port"));
            String sid = props.getProperty("db.sid");
            String user = props.getProperty("db.user");
            String pwd = props.getProperty("db.password");
            Integer poolSize = Integer.valueOf(props.getProperty("db.poolsize"));
            //
            ds.setMaximumPoolSize(2);
            ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
             jdbcURL = "jdbc:oracle:thin:@"+host+":"+port+"/"+ sid;
            ds.setJdbcUrl(jdbcURL); ;
            ds.setUsername(user);
            ds.setPassword(pwd);
            ds.setMaximumPoolSize(poolSize);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Oracle DS for url="+ jdbcURL+",  from file: " + CONFIG_FILE , e);
        }
        HikariConfig hc = new HikariConfig();
        hc.setDataSource(ds);
        HikariDataSource hdc = new HikariDataSource(hc);
        return hdc;
    }
    /**
     * Build a pg ds from
     * @return
     */
    private static DataSource pgDS() {
        final Properties props = new Properties();
        PGSimpleDataSource ds = null;
        final String CONFIG_FILE="ds-postgres.properties";
        try (final InputStream stream =
                     new Dummy().getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
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
            throw new RuntimeException("Error creating Oracle DS from file: " + CONFIG_FILE);
        }
        HikariConfig hc = new HikariConfig();
        hc.setDataSource(ds);
        hc.setMaximumPoolSize(2);
        HikariDataSource hdc = new HikariDataSource(hc);
        return hdc;
    }


    /**
     * Build an inmem ds
     * @return
     */
    public static DataSource inmemDS() {
        final Properties props = new Properties();
        org.h2.jdbcx.JdbcDataSource ds ;
        ds = new org.h2.jdbcx.JdbcDataSource();
        ds.setURL(datasourceH2());
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

    public static Model.Case buildSubsystemCase(Model.Xref xref, Model.Case ofCase) {
        return Model.Case.newBuilder()
                .setId(xref.getXrefId()) //TODO incomplete - set typeProps,
                .setTypeId(ofCase.getTypeId())
                .setDescription(ofCase.getDescription())
                .setReportedByCustomer(ofCase.getReportedByCustomer())
                .setCreatedByEmployee(ofCase.getCreatedByEmployee())
                .setAddress(ofCase.getAddress())
                .setStatus(ofCase.getStatus())
                //.putXrefAccounts()//No xref
                .build();
    }
    private static class Dummy {/*for prop file load*/
    }

}


    /*
    // Use for junit
    @Deprecated
    public static SubsystemService inMem() {
        return withDs(inmemDS());
    }

    @Deprecated
    private static SubsystemService withDs(DataSource ds) {
        CRUDDao<Model.Person> personDao = JdbiDbUtil.getDao(ds, Model.Person.class);
        CRUDDao<Model.Xref> xrefDao = JdbiDbUtil.getDao(ds, Model.Xref.class);
        CRUDDao<Model.PostalAddress> addressDao = JdbiDbUtil.getDao(ds, Model.PostalAddress.class);
        CRUDDao<Model.Case> caseDao = JdbiDbUtil.getDao(ds, Model.Case.class);
        return new IntegSubsystemServiceImpl(personDao, xrefDao, addressDao, caseDao);
    }*/
