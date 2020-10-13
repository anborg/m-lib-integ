package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(PersonMapper.class)
@UseClasspathSqlLocator
interface DaoDelegateInterfaceAddress extends CRUDDao<Model.PostalAddress> {

//    @GetGeneratedKeys
//    @SqlUpdate("INSERT INTO integ.INTG_person(firstname, lastname, email, phone1, phone2, addr_id) VALUES ( :firstName, :lastName, :contacts)")
//    int insert(@BindBean Model.Person person);
//
//    @SqlUpdate("update integ.INTG_person set firstname = :firstName, lastname = :lastName where id = cast(:id as INTEGER )")
//    void update(@BindBean Model.Person person);
//
//    @SqlQuery("select * from integ.INTG_person where id = :id")
//    @RegisterBeanMapper(PersonMapper.class)
//    Model.Person get(@Bind("id")int id);

    @Deprecated
    @SqlQuery("select * from integ.INTG_ADDRESS ")
    List<Model.PostalAddress> getAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person")
    void deleteAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person where id = cast(:id as INTEGER )")
    void delete(@Bind("id") int id);
}//Dao