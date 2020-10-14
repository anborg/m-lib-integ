package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(MapperPerson.class)
@UseClasspathSqlLocator
interface DaoDelegateInterfaceAddress extends CRUDDao<Model.PostalAddress> {

    @GetGeneratedKeys
    @SqlUpdate("insert into INTEG_ADDRESS (streetnum,streetname,postalcode,city,country,lat,lon) values ( :streetnum,:streetname,:postalcode,:city,:country,:lat,:lon)")
    Long insert(@BindBean Model.PostalAddress in);

    @SqlUpdate("update integ.INTEG_ADDRESS set streetnum=:streetnum   streetname=:streetname,postalcode=:postalcode,city=:city,country=:country,lat=:lat,lon=:lon where id = cast(:id as INTEGER)")
    Long update(@BindBean Model.PostalAddress in);

    @SqlQuery("select * from INTEG_ADDRESS where id = :id")
    @RegisterBeanMapper(MapperAddress.class)
    Model.PostalAddress get(@Bind("id") int id);

    @Deprecated
    @SqlQuery("select * from INTEG_ADDRESS ")
    List<Model.PostalAddress> getAll();

    @Deprecated
    @SqlUpdate("delete from INTEG_ADDRESS")
    void deleteAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTEG_ADDRESS where id = cast(:id as INTEGER )")
    void delete(@Bind("id") int id);
}//Dao