package integ.dao.jdbi;

import muni.dao.PersonDao;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
@RegisterBeanMapper(PersonMapper.class)
interface PersonDaoDelegateInterface extends PersonDao {
    @SqlUpdate("CREATE TABLE integ.INTG_person (id  SERIAL PRIMARY KEY, firstname VARCHAR, lastname VARCHAR)")
    void createTable();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO integ.INTG_person(firstname, lastname) VALUES ( :firstName, :lastName)")
    int insert(@BindBean Model.Person person);

    @SqlUpdate("update integ.INTG_person set firstname = :firstName, lastname = :lastName where id = cast(:id as INTEGER )")
    void update(@BindBean Model.Person person);

    @SqlQuery("select * from integ.INTG_person where id = :id")
    @RegisterBeanMapper(PersonMapper.class)
    Model.Person get(@Bind("id")int id);

    @Deprecated
    @SqlQuery("select * from integ.INTG_person order by id")

    List<Model.Person> getAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person")
    void deleteAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTG_person where id = cast(:id as INTEGER )")
    void delete(@Bind("id")int id);
}//Dao