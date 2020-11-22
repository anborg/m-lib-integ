package integ.dao.jdbi;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//mapper -- THe order of building may need to follow this standard pattern.
class RowMapperXref implements RowMapper<Model.Xref.Builder> {
    @Override
    public Model.Xref.Builder map(ResultSet rs, StatementContext ctx) throws SQLException {
        Model.Xref.Builder xref = Model.Xref.newBuilder();

        final Model.PostalAddress.Builder ab = Model.PostalAddress.newBuilder();
        String master_PersonId = "" + rs.getLong("x_id"); //TODO add to obj?
        if (null == master_PersonId) return null;//TODO make not of this nul return.
        String xref_sys_id = rs.getString("x_xref_sys_id");
        String xref_person_id = rs.getString("x_xref_person_id");
        //set
        Optional.ofNullable(master_PersonId).ifPresent(xref::setId);
        Optional.ofNullable(xref_sys_id).ifPresent(xref::setXrefSystemId);
        Optional.ofNullable(xref_person_id).ifPresent(xref::setXrefId);
        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("x_ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("x_ts_update").toInstant().getEpochSecond());
        var ts_ss_refreshed = Timestamps.fromSeconds(rs.getTimestamp("x_ts_refreshed").toInstant().getEpochSecond());
        Optional.ofNullable(ts_create).ifPresent(xref::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(xref::setUpdateTime);
        Optional.ofNullable(ts_ss_refreshed).ifPresent(xref::setSubsysRefreshTime);
        return xref;
    }
}//mapper