package integ.dao.jdbi;

import access.integ.Queries;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(MapperPerson.class)
interface DaoDelegateInterfacePerson extends CRUDDao<Model.Person> {

    @Deprecated
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO integ.INTEG_PERSON(firstname, lastname, email, phone1, phone2, address_id) VALUES ( :firstName, :lastName, :email, :phone1, :phone2, :addressId")
    Long save(@BindBean Model.Person person);


    @Deprecated
    @SqlQuery(Queries.sql_person_select)
    @RegisterBeanMapper(MapperPerson.class)
    Optional<Model.Person> get(@Bind("id") long id);

    @SqlUpdate("insert into integ.INTEG_PERSON(firstname, lastname, email, phone1, phone2) VALUES ( :firstName, :lastName, :email, :phone1, :phone2 )")
    Integer insert(@Bind("firstName") String firstName,
                   @Bind("lastName") String lastName,
                   @Bind("email") String email,
                   @Bind("phone1") String phone1,
                   @Bind("phone2") String phone2
                   //@Bind("addressIdParam") Long addressId
    );

    @SqlUpdate("update integ.INTEG_PERSON set firstname,=:firstName,lastname=:lastName, email=:email, phone1=:phone1, phone2=:phone2, :addressIdParam where id=cast(:id as INTEGER)")
    long update(@Bind("firstName") String firstName,
                @Bind("lastName") String lastName,
                @Bind("email") String email,
                @Bind("phone1") String phone1,
                @Bind("phone2") String phone2,
                @Bind("addressIdParam") Long addressId);

    @Deprecated
    @SqlQuery("select * from integ.INTEG_PERSON ")
    List<Model.Person> getAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTEG_PERSON")
    void deleteAll();

    @Deprecated
    @SqlUpdate("delete from integ.INTEG_PERSON where id = cast(:id as INTEGER )")
    void delete(@Bind("id") long id);
}//Dao