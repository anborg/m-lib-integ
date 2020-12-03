package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
// All select queries:@Transaction(TransactionIsolationLevel.READ_COMMITTED)
//@RegisterBeanMapper(RowMapperPersonWithAddress.class) //DO NOT ADD mapper at class level. Add at method level //TODO make note.
interface JdbiDao {//extends CRUDDao<Model.Person>
    interface person {
        @Deprecated
        @SqlQuery(Queries.sql_person_select_byId)
        @UseRowReducer(ReducerPersonAddressXref.class)
        @RegisterBeanMapper(value = Model.Person.class, prefix = "p")
        @RegisterBeanMapper(value = Model.PostalAddress.class, prefix = "a")
        @RegisterBeanMapper(value = Model.Xref.class, prefix = "x")
        @Transaction(TransactionIsolationLevel.READ_COMMITTED)
        Optional<Model.Person.Builder> get(@Bind("id") Long id);

        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_PERSON(firstname, lastname, email, phone1, phone2) VALUES ( :firstName, :lastName, :email, :phone1, :phone2 )")
// RETURNING id
        Long insert(@BindBean Model.Person in);

        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_PERSON(firstname, lastname, email, phone1, phone2, address_id) VALUES ( :firstName, :lastName, :email, :phone1, :phone2, :addressId )")
// RETURNING id
        Long insert(@BindBean Model.Person in, @Bind("addressId") Long addressId);


        @GetGeneratedKeys
        @SqlUpdate("update INTEG_PERSON set firstname=:firstName,lastname=:lastName, email=:email, phone1=:phone1, phone2=:phone2 where id=cast(:id as INTEGER)")
        Long update(@BindBean Model.Person in);

        @GetGeneratedKeys
        @SqlUpdate("update INTEG_PERSON set firstname=:firstName,lastname=:lastName, email=:email, phone1=:phone1, phone2=:phone2, address_id=:addressId where id=cast(:id as INTEGER)")
        Long update(@BindBean Model.Person in, @Bind("addressId") Long addressId);

        @Deprecated
        @SqlQuery(Queries.sql_person_select_all)
        @UseRowReducer(ReducerPersonAddressXref.class)
        @RegisterBeanMapper(value = Model.Person.class, prefix = "p")
        @RegisterBeanMapper(value = Model.PostalAddress.class, prefix = "a")
        @RegisterBeanMapper(value = Model.Xref.class, prefix = "x")
        @Transaction(TransactionIsolationLevel.READ_COMMITTED)
        List<Model.Person.Builder> getAll();

        @Deprecated
        @SqlUpdate("delete from INTEG_PERSON")
        void deleteAll();

        @Deprecated
        @SqlUpdate("delete from INTEG_PERSON where id = cast(:id as INTEGER )")
        void delete(@Bind("id") Long id);

    }


    interface address {
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
        @Transaction(TransactionIsolationLevel.READ_COMMITTED)
        String hasAddress(@Bind("id") String id);

    }


    //    @GetGeneratedKeys
//    @SqlUpdate("INSERT INTO INTEG_PERSON(firstname, lastname, email, phone1, phone2 VALUES ( :firstName, :lastName, :email, :phone1, :phone2)")
//    Long insert(@BindBean Model.Person in);
    interface xref {
        //For Insert/Update master_person_id is provided separately, to avoid setting Xref.id and then supplying here.
        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id, xref_person_id) VALUES ( cast(:master_person_id AS INTEGER), :xrefSystemId, :xrefId )")
        Long insert(@Bind("master_person_id") Long masterPersonId, @BindBean Model.Xref in);

        @Transactional
        //@GetGeneratedKeys
        @SqlUpdate("update INTEG_XREF_PERSON set  xref_person_id=:xrefId where id = cast(:master_person_id AS INTEGER) and xref_sys_id =:xrefSystemId,")
        Long update(@Bind("master_person_id") Long masterPersonId, @BindBean Model.Xref in);

                @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id, xref_person_id) VALUES ( cast(:id AS INTEGER), :xrefSystemId, :xrefId )")
        Long insert(@BindBean Model.Xref in);


        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id) VALUES ( :person_id, :xrefSystemId)")
            // RETURNING id
        Long recordIntentPersonXref(@Bind("person_id") Long personId, @Bind("subsysId") String xrefSubsysId);


        @SqlQuery("SELECT id, xref_sys_id, xref_person_id, ts_create, ts_update,ts_ss_refreshed from INTEG_XREF_PERSON where id = cast(:masterPersonId AS INTEGER)")
        @RegisterBeanMapper(RowMapperXref.class)
        @Transaction(TransactionIsolationLevel.READ_COMMITTED)
        List<Model.Xref> getXrefPerson(@Bind("masterPersonId") Long masterPersonId);


    }

    interface acase {

    }

    class ReducerPersonAddressXref implements LinkedHashMapRowReducer<Long, Model.Person.Builder> {

        @Override
        public void accumulate(Map<Long, Model.Person.Builder> map, RowView rowView) {
            Model.Person.Builder p = map.computeIfAbsent(rowView.getColumn("p_id", Long.class),
                    id -> rowView.getRow(Model.Person.Builder.class));

            //Address for Person.
            if (!p.hasAddress() && rowView.getColumn("a_id", Long.class) != null) {
                Model.PostalAddress.Builder addr = rowView.getRow(Model.PostalAddress.Builder.class);
                p.setAddress(addr);
            }

            //XREF for Person
            String sysId = rowView.getColumn("x_xref_sys_id", String.class);
            if (Objects.nonNull(sysId) && Objects.isNull(p.getXrefsMap().get(sysId)) ) {
                Model.Xref.Builder xref = rowView.getRow(Model.Xref.Builder.class);
                p.putXrefs(xref.getXrefSystemId(), xref.build());
            }
        }
    }//ReducerPersonAddressXref

}//JdbiDao


