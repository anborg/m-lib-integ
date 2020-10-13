package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(PersonMapper.class)
interface DaoDelegateInterfacePerson extends CRUDDao<Model.Person> {
//    @SqlUpdate("CREATE TABLE integ.INTG_person (id  SERIAL PRIMARY KEY, firstname VARCHAR, lastname VARCHAR, email VARCHAR(20), phone1 VARCHAR(15), phone2 VARCHAR(15)")
//    void createTable();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO integ.INTG_person(firstname, lastname, email, phone1, phone2, address_id) VALUES ( :firstName, :lastName")
    long save(@BindBean Model.Person person);

//    @SqlUpdate("update integ.INTG_person set firstname = :firstName, lastname = :lastName where id = cast(:id as INTEGER )")
//    void update(@BindBean Model.Person person);

    @SqlQuery(IntegUtil.sql_select_person)
    @RegisterBeanMapper(PersonMapper.class)
    Model.Person get(@Bind("id") long id);

    @Deprecated
    @SqlQuery("select * from integ.INTG_person order by id")
    List<Model.Person> getAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person")
    void deleteAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person where id = cast(:id as INTEGER )")
    void delete(@Bind("id") long id);
}//Dao