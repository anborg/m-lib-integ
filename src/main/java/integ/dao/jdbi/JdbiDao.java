package integ.dao.jdbi;

import access.integ.Queries;
import muni.model.Model;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//@RegisterBeanMapper(MapperPersonWithAddress.class) //DO NOT ADD mapper at class level. Add at method level //TODO make note.
class JdbiDao {//extends CRUDDao<Model.Person>

    //    @GetGeneratedKeys
//    @SqlUpdate("INSERT INTO integ.INTEG_PERSON(firstname, lastname, email, phone1, phone2 VALUES ( :firstName, :lastName, :email, :phone1, :phone2)")
//    Long insert(@BindBean Model.Person in);
    public interface xref{
        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into integ.INTEG_XREF_PERSON (id, xref_sys_id, xref_person_id) VALUES ( :person_id, :xrefSysId, :xrefPersonId )")
// RETURNING id
        Long insert(@Bind("person_id") Long personId,@BindBean Model.Xref in);


        @SqlQuery("SELECT id, xref_sys_id, xref_person_id, ts_create, ts_update,ts_ss_refreshed from integ.INTEG_XREF_PERSON where id = :id")
        @RegisterBeanMapper(MapperXref.class)
        List<Model.Xref> getXrefPerson(@Bind("id") Long id);
    }

    public interface person {
        @Deprecated
        @SqlQuery(Queries.sql_person_select_byId)
        @RegisterBeanMapper(MapperPersonWithAddress.class)
        Optional<Model.Person> get(@Bind("id") long id);

        @GetGeneratedKeys
        @SqlUpdate("insert into integ.INTEG_PERSON(firstname, lastname, email, phone1, phone2) VALUES ( :firstName, :lastName, :email, :phone1, :phone2 )")
// RETURNING id
        Long insert(@BindBean Model.Person in);

        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into integ.INTEG_PERSON(firstname, lastname, email, phone1, phone2, address_id) VALUES ( :firstName, :lastName, :email, :phone1, :phone2, :addressId )")
// RETURNING id
        Long insert(@BindBean Model.Person in, @Bind("addressId") Long addressId);


        @GetGeneratedKeys
        @SqlUpdate("update integ.INTEG_PERSON set firstname=:firstName,lastname=:lastName, email=:email, phone1=:phone1, phone2=:phone2 where id=cast(:id as INTEGER)")
        Long update(@BindBean Model.Person in);

        @GetGeneratedKeys
        @SqlUpdate("update integ.INTEG_PERSON set firstname=:firstName,lastname=:lastName, email=:email, phone1=:phone1, phone2=:phone2, address_id=:addressId where id=cast(:id as INTEGER)")
        Long update(@BindBean Model.Person in, @Bind("addressId") Long addressId);

        @Deprecated
        @SqlQuery(Queries.sql_person_select_all)
        List<Model.Person> getAll();

        @Deprecated
        @SqlUpdate("delete from integ.INTEG_PERSON")
        void deleteAll();

        @Deprecated
        @SqlUpdate("delete from integ.INTEG_PERSON where id = cast(:id as INTEGER )")
        void delete(@Bind("id") long id);

    }

    public interface address {
        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_ADDRESS (streetnum,streetname,postalcode,city,country,lat,lon) values ( :streetNum,:streetName,:postalCode,:city,:country,:lat,:lon)")
        Long insert(@BindBean Model.PostalAddress in);

        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_ADDRESS (streetnum,streetname,postalcode,city,country,lat,lon) values ( :streetNum,:streetName,:postalCode,:city,:country,:lat,:lon)")
        Long update(@BindBean Model.PostalAddress in);

        //TODO find hasOne mechanism for this : see github jdbi fix.
        @SqlQuery("select 1 from INTEG_ADDRESS where id=cast(:id as INTEGER ) LIMIT 1")
        String hasAddress(@Bind("id") String id);

    }

    public interface acase {

    }
}//Dao