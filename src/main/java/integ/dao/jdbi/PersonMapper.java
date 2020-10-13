package integ.dao.jdbi;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

//mapper
class PersonMapper implements RowMapper<Model.Person> {
    @Override
    public Model.Person map(ResultSet rs, StatementContext ctx) throws SQLException {
        Timestamp ts = Timestamps.fromSeconds(rs.getTimestamp("ts_create").toInstant().getEpochSecond());
        return Model.Person.newBuilder()
                .setId("" + rs.getInt("id"))
                .setFirstName(rs.getString("firstname"))
                .setLastName(rs.getString("lastname"))
                .setContactChannels(Model.ContactChannels.newBuilder()
                        //TODO consider changing email to simple?
                        .setEmail(rs.getString("email"))
                        //TODO change phone num to string // consier chnaging phone to simple?
                        .setPhone1(Model.Phone.newBuilder().setNumber(rs.getString("phone1")))
                        .setPhone2(Model.Phone.newBuilder().setNumber(rs.getString("phone2")))
                        .setPostalAddress(Model.PostalAddress.newBuilder()
                                .setId(rs.getString("address_id"))
                                .setStreetNum(rs.getString("streetnum"))
                                .setStreetName(rs.getString("streetname"))
                                .setCity(rs.getString("city"))
                                .setCountry(rs.getString("country"))
                                .setPostalCode(rs.getString("postalcode"))
                                .setLat(rs.getLong("lat"))
                                .setLon(rs.getLong("lon"))
                                .setCreateTime(Timestamps.fromSeconds(rs.getTimestamp("addr_ts_create").toInstant().getEpochSecond()))
                                .setUpdateTime(Timestamps.fromSeconds(rs.getTimestamp("addr_ts_update").toInstant().getEpochSecond()))
                                .build()//addr
                        ).build())//channels
                .setCreateTime(ts)
                .setUpdateTime(Timestamps.fromSeconds(rs.getTimestamp("ts_update").toInstant().getEpochSecond()))
                .build();//person
    }
}//mapper