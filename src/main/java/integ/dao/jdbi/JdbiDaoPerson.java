package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
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

interface JdbiDaoPerson {//extends CRUDDao<Model.Person>
    //interface person {

    @UseClasspathSqlLocator
    //TODO can target package name be added as annotationparam here [change req]?  //e.g @UseClasspathSqlLocator(packageName=integ.dao.jdbi)
    //TODO Can it look in resources/integ_dao_sql/<filename>.sql , so all sql are grouped in one resource folder?
    @SqlQuery("select_Person_byId")
    //TODO Why can't it just look for package name? It looks in inner interface name, and fails.
    @UseRowReducer(ReducerPersonAddressXref.class)
    @RegisterBeanMapper(value = Model.Person.class, prefix = "p")
    @RegisterBeanMapper(value = Model.PostalAddress.class, prefix = "a")
    @RegisterBeanMapper(value = Model.Xref.class, prefix = "x")
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    Optional<Model.Person.Builder> get(@Bind("id") Long id);

    @Transactional
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
    @SqlQuery("select_Persons_top_x")
    @UseClasspathSqlLocator(stripComments = true)
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

    //}


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



    //Keep this inside, and package-access
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
            if (Objects.nonNull(sysId) && Objects.isNull(p.getXrefsMap().get(sysId))) {
                Model.Xref.Builder xref = rowView.getRow(Model.Xref.Builder.class);
                p.putXrefs(xref.getXrefSystemId(), xref.build());
            }
        }
    }//ReducerPersonAddressXref


}//JdbiDaoPerson


