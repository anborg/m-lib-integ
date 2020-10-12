package integ.jdbi.dao;

import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

//mapper
class PersonMapper implements RowMapper<Model.Person> {
    @Override
    public Model.Person map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Model.Person.newBuilder()
                .setId(""+rs.getInt("id"))
                .setFirstName(rs.getString("firstname"))
                .setLastName(rs.getString("lastname"))
                .build();
    }
}//mapper