package integ.dao.jdbi;

import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

//mapper -- THe order of building may need to follow this standard pattern.
class RowMapperCase implements RowMapper<Model.Case.Builder> {
    private static Logger logger = Logger.getLogger(RowMapperCase.class.getName());
    @Override
    public Model.Case.Builder map(ResultSet rs, StatementContext ctx) throws SQLException {
        Model.Case.Builder cb = Model.Case.newBuilder();
        Long id = rs.getLong("c_id");
        logger.info("Retrieved case id : " + id);
        if (null == id) return null;//TODO make not of this nul return.
        //build person
        String status = rs.getString("c_status");
        String type_id = rs.getString("c_type_id");
        String reportedby_person_id = rs.getString("c_reportedby_person_id");
        String createdby_emp_id = rs.getString("c_createdby_emp_id");
        String address_id = rs.getString("c_address_id");
        String description = rs.getString("c_description");
        //Timestamps are guaranteed to be in db.
        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("c_ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("c_ts_update").toInstant().getEpochSecond());

        Optional.ofNullable(id).ifPresent(cb::setId);
        Optional.ofNullable(status).ifPresent(cb::setStatus);
        Optional.ofNullable(type_id).ifPresent(cb::setTypeId);
//        Optional.ofNullable(reportedby_person_id).ifPresent(pb:);
//        Optional.ofNullable(createdby_emp_id).ifPresent(pb::setCreatedByEmployee);
        //if (Objects.nonNull(address_id)) Optional.ofNullable(ab.build()).ifPresent(pb::setAddress); //handled in Reducer.
        Optional.ofNullable(description).ifPresent(cb::setDescription);

        //pb.putXrefAccounts() // handled in reducer
        Optional.ofNullable(ts_create).ifPresent(cb::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(cb::setUpdateTime);
        logger.info("Case timestamp :" + ts_create  );

        return cb;
    }
}//mapper