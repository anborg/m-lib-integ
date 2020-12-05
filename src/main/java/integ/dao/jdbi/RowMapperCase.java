package integ.dao.jdbi;

import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//mapper -- THe order of building may need to follow this standard pattern.
class RowMapperCase implements RowMapper<Model.Case.Builder> {
    @Override
    public Model.Case.Builder map(ResultSet rs, StatementContext ctx) throws SQLException {
        Model.Case.Builder pb = Model.Case.newBuilder();
        String id = "" + rs.getLong("c_id");
        System.out.println("Retrieved case id : " + id);
        if (null == id) return null;//TODO make not of this nul return.
        //build person
        String status = rs.getString("c_status");
        String type_id = rs.getString("c_type_id");
        String reportedby_person_id = rs.getString("c_reportedby_person_id");
        String createdby_emp_id = rs.getString("c_createdby_emp_id");
        String address_id = rs.getString("c_address_id");
        String description = rs.getString("c_description");

        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("c_ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("c_ts_update").toInstant().getEpochSecond());

        Optional.ofNullable(id).ifPresent(pb::setId);
        Optional.ofNullable(status).ifPresent(pb::setStatus);
        Optional.ofNullable(type_id).ifPresent(pb::setTypeId);
//        Optional.ofNullable(reportedby_person_id).ifPresent(pb:);
//        Optional.ofNullable(createdby_emp_id).ifPresent(pb::setCreatedByEmployee);
        //if (Objects.nonNull(address_id)) Optional.ofNullable(ab.build()).ifPresent(pb::setAddress); //handled in Reducer.
        Optional.ofNullable(description).ifPresent(pb::setDescription);

        //pb.putXrefAccounts() // handled in reducer
        Optional.ofNullable(ts_create).ifPresent(pb::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(pb::setUpdateTime);


        return pb;
    }
}//mapper