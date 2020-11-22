package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.result.ResultSetAccumulator;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RsAccumulatorPersonAddressXref implements ResultSetAccumulator<Model.Person> {

    private final Model.Person.Builder out = Model.Person.newBuilder();

    // this mapping method will get called for every row in the result set
    public Model.Person map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        //Get all fields here
        String integ_PersonId = "" + rs.getLong("id");


        // for the first row of the result set, we create the wrapper object
        if (index == 0) {
            //set ONE-ONE  instance fields
            //set Person ONE INSTANCE
            out.setId(integ_PersonId);
            //set ONE-RIGHT-side instance i.e address
            if (!out.hasAddress()) {
                Model.PostalAddress addr = Model.PostalAddress.newBuilder()
                        .setStreetNum("" + rs.getLong("street_num")).build();
                out.setAddress(addr);
            }
        }


        String xref_sys_id = rs.getString("xref_sys_id");
        String xref_person_id = rs.getString("xref_person_id");
        // ...and with every line we add one of the joined users
        Model.Xref xref = Model.Xref.newBuilder()
                .setId(integ_PersonId)
                .setXrefSystemId(xref_sys_id)
                .setXrefId(xref_person_id)
                .build();
        out.putXrefAccounts(xref_sys_id, xref);
        return out.build();
    }

    @Override
    public Model.Person apply(Model.Person previous, ResultSet rs, StatementContext ctx) throws SQLException {
        return null;
    }
}