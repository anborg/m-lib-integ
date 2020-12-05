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

    Long create(Model.Xref in);

    Long create(Model.Case in);
    Optional<Model.Case> getCase(String id);
    List<Model.Case> getRecentCases();
    Model.Case update(Model.Case in);

    Model.Case recordIntentXref(Model.Case in, Subsys subsysType);


}
