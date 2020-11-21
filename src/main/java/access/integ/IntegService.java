package access.integ;

import muni.model.Model;
import muni.service.SubsystemService;

import java.util.Optional;

public interface IntegService {
    Model.Person create(Model.Person in);

    Optional<Model.Person> get(String id);

    Model.Person update(Model.Person in);

    Model.Person recordIntentXref(Model.Person in, Subsys subsysType);

    Model.Case create(Model.Case in);

    Model.Case update(Model.Case in);

    Model.Case recordIntentXref(Model.Case in, Subsys subsysType);

    void setSubSystemService(Subsys amanda, SubsystemService amandaService);
}
