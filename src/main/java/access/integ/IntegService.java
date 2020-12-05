package access.integ;

import muni.model.Model;
import muni.service.SubsystemService;

import java.util.List;
import java.util.Optional;

public interface IntegService {
    Model.Person create(Model.Person in);

    Optional<Model.Person> getPerson(String id);

    List<Model.Person> personsRecent();

    //Only for test validation?
    Optional<Model.Person> getSubsystemPerson(Model.Xref xref);


    Model.Person update(Model.Person in);

    Model.Person recordIntentXref(Model.Person in, Subsys subsysType);





    Optional<Model.Case> getCase(String id);
    Model.Case create(Model.Case in);

    Model.Case update(Model.Case in);
    List<Model.Case> casesRecent();

    Model.Case recordIntentXref(Model.Case in, Subsys subsysType);

    void setSubsystemService(Subsys subsys, SubsystemService subsystemService);
    // for testing
    SubsystemService getSubsystemService(String subsys);
}
