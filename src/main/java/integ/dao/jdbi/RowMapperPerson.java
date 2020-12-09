package integ.dao.jdbi;

import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//mapper -- THe order of building may need to follow this standard pattern.
class RowMapperPerson implements RowMapper<Model.Person.Builder> {
    @Override
    public Model.Person.Builder map(ResultSet rs, StatementContext ctx) throws SQLException {
        Model.Person.Builder pb = Model.Person.newBuilder();
        Long id = rs.getLong("p_id");
        System.out.println("Retrieved person id : " + id);
        if (null == id) return null;//TODO make not of this nul return.
        //build person
        String firstName = rs.getString("p_firstname");
        String lastName = rs.getString("p_lastname");
        String email = rs.getString("p_email");
        String phone1 = rs.getString("p_phone1");
        String phone2 = rs.getString("p_phone2");
        //Timestamps are guaranteed to be in db.
        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("p_ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("p_ts_update").toInstant().getEpochSecond());

        Optional.ofNullable(id).ifPresent(pb::setId);
        Optional.ofNullable(firstName).ifPresent(pb::setFirstName);
        Optional.ofNullable(lastName).ifPresent(pb::setLastName);
        Optional.ofNullable(email).ifPresent(pb::setEmail);
        Optional.ofNullable(phone1).ifPresent(pb::setPhone1);
        Optional.ofNullable(phone2).ifPresent(pb::setPhone2);
        //if (Objects.nonNull(address_id)) Optional.ofNullable(ab.build()).ifPresent(pb::setAddress); //handled in Reducer.
        //pb.putXrefAccounts() // handled in reducer
        Optional.ofNullable(ts_create).ifPresent(pb::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(pb::setUpdateTime);


        return pb;
    }
}//mapper