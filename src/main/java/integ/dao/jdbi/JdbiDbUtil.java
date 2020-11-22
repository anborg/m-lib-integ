package integ.dao.jdbi;

import access.integ.IntegDao;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class JdbiDbUtil {
    private static void common(Jdbi jdbi) {
        jdbi.installPlugin(new SqlObjectPlugin())
                //common mappers
                .registerRowMapper(new RowMapperPerson())
                .registerRowMapper(new RowMapperAddress())
                .registerRowMapper(new RowMapperXref())
        ;
    }

    public static Jdbi withDbPlugin(DataSource ds) {
        final Jdbi jdbi = Jdbi.create(ds);
        final DatabaseMetaData dbmeta;
        try (Connection con = ds.getConnection()) {
            dbmeta = con.getMetaData();
            final var dbProductName = dbmeta.getDatabaseProductName();
            System.out.println("dbproductname=" + dbProductName);
            switch (dbProductName) {
                case "PostgreSQL":
                    jdbi.installPlugin(new PostgresPlugin());
                    break;
                case "H2":
                    jdbi.installPlugin(new H2DatabasePlugin());
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        common(jdbi);
        return jdbi;
    }

    public static Jdbi buildJdbiH2(DataSource ds) {
        Jdbi jdbi = Jdbi.create(ds);
        jdbi.installPlugin(new H2DatabasePlugin());
        common(jdbi);
        return jdbi;
    }

    public static IntegDao getIntegDao(DataSource ds) {
        //if (!type.isInterface()) throw new RuntimeException("Only interface type expected");
        return new DaoImpl(withDbPlugin(ds));
    }

    @Deprecated
    public static <T> CRUDDao<T> getDao(DataSource ds, Class<T> type) {
        //if (!type.isInterface()) throw new RuntimeException("Only interface type expected");

        if (type.getName().equals(Model.Person.class.getName())) {
            return (CRUDDao<T>) new DaoImplPerson(withDbPlugin(ds));
        }
        if (type.getName().equals(Model.Xref.class.getName())) {
            return (CRUDDao<T>) new DaoImplXref(withDbPlugin(ds));
        }
        if (type.getName().equals(Model.PostalAddress.class.getName())) {
            return (CRUDDao<T>) new DaoImplAddress(withDbPlugin(ds));
        }

        if (type.getName().equals(Model.Case.class.getName())) {
            return (CRUDDao<T>) new DaoImplCase(withDbPlugin(ds));
        }

        throw new UnsupportedOperationException("Interface not expected" + type.getName());
    }

}
