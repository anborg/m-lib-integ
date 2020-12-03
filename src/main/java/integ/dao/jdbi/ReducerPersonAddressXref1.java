package integ.dao.jdbi;

import muni.model.Model;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Map;
import java.util.Objects;
// Creating protobuf-java objects from resultset.
class ReducerPersonAddressXref1 implements LinkedHashMapRowReducer<Long, Model.Person.Builder> {

        @Override
        public void accumulate(Map<Long, Model.Person.Builder> map, RowView rowView) {
            Model.Person.Builder p = map.computeIfAbsent(rowView.getColumn("p_id", Long.class),
                    id -> rowView.getRow(Model.Person.Builder.class));

            //Address for Person.
            if (!p.hasAddress() && rowView.getColumn("a_id", Long.class) != null) {
                Model.PostalAddress.Builder addr = rowView.getRow(Model.PostalAddress.Builder.class);
                p.setAddress(addr);
            }

            //XREF for Person
            String sysId = rowView.getColumn("x_xref_sys_id", String.class);
            if (Objects.nonNull(sysId) && Objects.isNull(p.getXrefsMap().get(sysId)) ) {
                Model.Xref.Builder xref = rowView.getRow(Model.Xref.Builder.class);
                p.putXrefs(xref.getXrefSystemId(), xref.build());
            }
        }
}//ReducerPersonAddressXref