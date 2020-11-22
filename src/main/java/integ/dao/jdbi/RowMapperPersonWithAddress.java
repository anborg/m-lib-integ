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
@Deprecated
class RowMapperPersonWithAddress implements RowMapper<Model.Person> {
    @Override
    public Model.Person map(ResultSet rs, StatementContext ctx) throws SQLException {
        Timestamp ts = Timestamps.fromSeconds(rs.getTimestamp("ts_create").toInstant().getEpochSecond());
        Model.Person.Builder pb = Model.Person.newBuilder();

        final Model.PostalAddress.Builder ab = Model.PostalAddress.newBuilder();
        String id = "" + rs.getInt("id");
        System.out.println("Retrieved person id : " + id);
        if (null == id) return null;//TODO make not of this nul return.
        //build address
        String address_id = rs.getString("address_id");


        if (Objects.nonNull(address_id)) {
            String streetNum = rs.getString("streetnum");
            String streetName = rs.getString("streetname");
            String city = rs.getString("city");
//        String province =rs.getString("province");
            String country = rs.getString("country");
            String postalCode = rs.getString("postalcode");
            Long lat = rs.getLong("lat");
            Long lon = rs.getLong("lon");
            var addr_ts_create = rs.getTimestamp("addr_ts_create");
            var addr_ts_update = rs.getTimestamp("addr_ts_update");

            Optional.ofNullable(address_id).ifPresent(ab::setId);
            Optional.ofNullable(streetNum).ifPresent(ab::setStreetNum);
            Optional.ofNullable(streetName).ifPresent(ab::setStreetName);
            Optional.ofNullable(city).ifPresent(ab::setCity);
            Optional.ofNullable(country).ifPresent(ab::setCountry);
//            Optional.ofNullable(province).ifPresent(ab::setProvince);
            Optional.ofNullable(postalCode).ifPresent(ab::setPostalCode);
            Optional.ofNullable(lat).ifPresent(ab::setLat);
            Optional.ofNullable(lon).ifPresent(ab::setLon);
            Optional.ofNullable(addr_ts_create).ifPresent(t -> ab.setCreateTime(Timestamps.fromSeconds(t.toInstant().getEpochSecond())));
            Optional.ofNullable(addr_ts_update).ifPresent(t -> ab.setUpdateTime(Timestamps.fromSeconds(t.toInstant().getEpochSecond())));
        }
        //build person

        String firstName = rs.getString("firstname");
        String lastName = rs.getString("lastname");
        String email = rs.getString("email");
        String phone1 = rs.getString("phone1");
        String phone2 = rs.getString("phone2");
        var ts_create = Timestamps.fromSeconds(rs.getTimestamp("ts_create").toInstant().getEpochSecond());
        var ts_update = Timestamps.fromSeconds(rs.getTimestamp("ts_update").toInstant().getEpochSecond());

        Optional.ofNullable(id).ifPresent(pb::setId);
        Optional.ofNullable(firstName).ifPresent(pb::setFirstName);
        Optional.ofNullable(lastName).ifPresent(pb::setLastName);
        Optional.ofNullable(email).ifPresent(pb::setEmail);
        Optional.ofNullable(phone1).ifPresent(pb::setPhone1);
        Optional.ofNullable(phone2).ifPresent(pb::setPhone2);
        if (Objects.nonNull(address_id)) Optional.ofNullable(ab.build()).ifPresent(pb::setAddress);
        Optional.ofNullable(ts_create).ifPresent(pb::setCreateTime);
        Optional.ofNullable(ts_update).ifPresent(pb::setUpdateTime);


        return pb.build();
    }
}//mapper