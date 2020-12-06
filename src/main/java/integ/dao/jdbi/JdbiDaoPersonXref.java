package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import javax.transaction.Transactional;
import java.util.List;

interface JdbiDaoPersonXref {
        //For Insert/Update master_person_id is provided separately, to avoid setting Xref.id and then supplying here.
        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id, xref_id) VALUES ( cast(:master_person_id AS INTEGER), :xrefSystemId, :xrefId )")
        Long insert(@Bind("master_person_id") Long masterPersonId, @BindBean Model.Xref in);

        @Transactional
        //@GetGeneratedKeys
        @SqlUpdate("update INTEG_XREF_PERSON set xref_id=:xrefId where id = cast(:master_person_id AS INTEGER) and xref_sys_id =:xrefSystemId,")
        Long update(@Bind("master_person_id") Long masterPersonId, @BindBean Model.Xref in);

        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id, xref_id) VALUES ( :id , :xrefSystemId, :xrefId )")
        Long insert(@BindBean Model.Xref in);


        @Transactional
        @GetGeneratedKeys
        @SqlUpdate("insert into INTEG_XREF_PERSON (id, xref_sys_id) VALUES ( :person_id, :xrefSystemId)")
                // RETURNING id
        Long recordIntentPersonXref(@Bind("person_id") Long personId, @Bind("subsysId") String xrefSubsysId);


        @SqlQuery("SELECT id, xref_sys_id, xref_id, ts_create, ts_update,ts_ss_refreshed from INTEG_XREF_PERSON where id = :masterPersonId ")
        @RegisterBeanMapper(RowMapperXref.class)
        @Transaction(TransactionIsolationLevel.READ_COMMITTED)
        List<Model.Xref> getXrefPerson(@Bind("masterPersonId") Long masterPersonId);


}
