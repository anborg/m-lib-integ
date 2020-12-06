package access.integ;

import muni.model.Model;

import java.util.List;
import java.util.Optional;

/**
 * Since IntegDaoImpl is in another package, I can't make it package access
 * This interface is not exposed external, so I can change method signatures.
 */
public interface IntegDao {
    Long create(Model.Person in);

    Optional<Model.Person> get(Long id);

    Model.Person update(Model.Person in);

    List<Model.Person> getRecentPersons();

    //Integration with subsystems
    @Deprecated
    Model.Person recordIntentXref(Model.Person in, Subsys subsysType);

    Long create(Model.Xref in);

    Long create(Model.Case in);

    Optional<Model.Case> getCase(Long id);

    List<Model.Case> getRecentCases();

    Model.Case update(Model.Case in);

    Model.Case recordIntentXref(Model.Case in, Subsys subsysType);


}
