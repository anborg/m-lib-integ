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

@UseClasspathSqlLocator
interface JdbiDaoCase {
    @Transactional
    @GetGeneratedKeys
    @SqlUpdate("insert_Case")
    Long insert(@BindBean Model.Case in);

    //@SqlQuery("SELECT c.id c_id, c.status c_status, c.type_id c_type_id, c.address_id c_address_id, reportedby_person_id c_reportedby_person_id, createdby_emp_id c_createdby_emp_id,c.description c_description , ts_create c_ts_create, ts_update c_ts_update from INTEG_CASE c where c.id = :id")

    @SqlQuery("select_Case_byId")
    @UseRowReducer(ReducerCaseAddressXref.class)
    @RegisterBeanMapper(value = Model.Case.class, prefix = "c")
    @RegisterBeanMapper(value = Model.PostalAddress.class, prefix = "a")
    @RegisterBeanMapper(value = Model.Xref.class, prefix = "x")
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    Optional<Model.Case.Builder> get(@Bind("id") Long id);

    //@SqlQuery("SELECT c.id c_id, c.status c_status, c.type_id c_type_id, c.address_id c_address_id, reportedby_person_id c_reportedby_person_id, createdby_emp_id c_createdby_emp_id,c.description c_description , ts_create c_ts_create, ts_update c_ts_update from INTEG_CASE c")
    @SqlQuery("select_Case_top_x")
    @UseRowReducer(ReducerCaseAddressXref.class)
    @RegisterBeanMapper(value = Model.Case.class, prefix = "c")
    @RegisterBeanMapper(value = Model.PostalAddress.class, prefix = "a")
    @RegisterBeanMapper(value = Model.Xref.class, prefix = "x")
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    List<Model.Case.Builder> getAll();

    @Transactional
    @SqlUpdate("update_Case")
    @GetGeneratedKeys
    Long update(@BindBean Model.Case in);


    class ReducerCaseAddressXref implements LinkedHashMapRowReducer<Long, Model.Case.Builder> {

        @Override
        public void accumulate(Map<Long, Model.Case.Builder> map, RowView rowView) {
            Model.Case.Builder p = map.computeIfAbsent(rowView.getColumn("c_id", Long.class),
                    id -> rowView.getRow(Model.Case.Builder.class));

            //Address for Person.
            if (!p.hasAddress() && rowView.getColumn("c_address_id", Long.class) != null) {
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
}