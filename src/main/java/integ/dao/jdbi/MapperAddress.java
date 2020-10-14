package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapperAddress implements RowMapper<Model.PostalAddress> {
    @Override
    public Model.PostalAddress map(ResultSet rs, StatementContext ctx) throws SQLException {
        return null;
    }
}
