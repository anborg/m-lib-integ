package integ.dao.jdbi;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

//mapper -- THe order of building may need to follow this standard pattern.
class MapperXref implements RowMapper<Model.Xref> {
    @Override
    public Model.Xref map(ResultSet rs, StatementContext ctx) throws SQLException {
        Timestamp ts = Timestamps.fromSeconds(rs.getTimestamp("ts_create").toInstant().getEpochSecond());
        Model.Xref.Builder xref = Model.Xref.newBuilder();

        final Model.PostalAddress.Builder ab = Model.PostalAddress.newBuilder();
        String person_id = "" + rs.getInt("id"); //TODO add to obj?
        if(null == person_id) return null;//TODO make not of this nul return.
        String xref_sys_id = rs.getString("xref_sys_id");
        String xref_person_id = rs.getString("xref_person_id");
        Optional.ofNullable(xref_sys_id).ifPresent(xref::setXrefSubsysId);
        Optional.ofNullable(xref_person_id).ifPresent(xref::setXrefPersonId);
        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("ts_update").toInstant().getEpochSecond());
        var ts_ss_refreshed = Timestamps.fromSeconds(rs.getTimestamp("ts_ss_refreshed").toInstant().getEpochSecond());
        Optional.ofNullable(ts_create).ifPresent(xref::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(xref::setUpdateTime);
        Optional.ofNullable(ts_ss_refreshed).ifPresent(xref::setSubsysRefreshTime);
        return xref.build();
    }
}//mapper