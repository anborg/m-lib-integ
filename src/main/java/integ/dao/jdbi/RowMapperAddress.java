package integ.dao.jdbi;

import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

//mapper -- THe order of building may need to follow this standard pattern.
class RowMapperAddress implements RowMapper<Model.PostalAddress.Builder> {

    @Override
    public Model.PostalAddress.Builder map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Model.PostalAddress.Builder ab = Model.PostalAddress.newBuilder();
        //build address
        String address_id = rs.getString("a_id");
        if (null == address_id) return null;//TODO make not of this nul return.
        if (Objects.nonNull(address_id)) {
            String streetNum = rs.getString("a_streetnum");
            String streetName = rs.getString("a_streetname");
            String city = rs.getString("a_city");
            //String province =rs.getString("a_province");
            String country = rs.getString("a_country");
            String postalCode = rs.getString("a_postalcode");
            Long lat = rs.getLong("a_lat");
            Long lon = rs.getLong("a_lon");
            var addr_ts_create = rs.getTimestamp("a_ts_create");
            var addr_ts_update = rs.getTimestamp("a_ts_update");

            Optional.ofNullable(address_id).ifPresent(ab::setId);
            Optional.ofNullable(streetNum).ifPresent(ab::setStreetNum);
            Optional.ofNullable(streetName).ifPresent(ab::setStreetName);
            Optional.ofNullable(city).ifPresent(ab::setCity);
            Optional.ofNullable(country).ifPresent(ab::setCountry);
            //Optional.ofNullable(province).ifPresent(ab::setProvince);
            Optional.ofNullable(postalCode).ifPresent(ab::setPostalCode);
            Optional.ofNullable(lat).ifPresent(ab::setLat);
            Optional.ofNullable(lon).ifPresent(ab::setLon);
            Optional.ofNullable(addr_ts_create).ifPresent(t -> ab.setCreateTime(Timestamps.fromSeconds(t.toInstant().getEpochSecond())));
            Optional.ofNullable(addr_ts_update).ifPresent(t -> ab.setUpdateTime(Timestamps.fromSeconds(t.toInstant().getEpochSecond())));
        }
        return ab;
    }
}//mapper