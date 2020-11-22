package access.integ;

import muni.model.Model;

import java.util.List;
import java.util.Optional;

public interface IntegDao {
    Long create(Model.Person in);

    Optional<Model.Person> get(String id);

    Model.Person update(Model.Person in);

    List<Model.Person> getRecentPersons();

    //Integration with subsystems
    @Deprecated
    Model.Person recordIntentXref(Model.Person in, Subsys subsysType);

    Long createOrUpdate(Model.Xref in);

    Model.Case create(Model.Case in);

    Model.Case update(Model.Case in);

    Model.Case recordIntentXref(Model.Case in, Subsys subsysType);


}
