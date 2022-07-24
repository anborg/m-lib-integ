package integ.test.service;

import access.integ.IntegService;
import access.integ.IntegUtil;
import access.integ.Subsys;
import muni.model.Model;
import muni.util.DataQuality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestIntegService_forPerson {

    IntegService service;

    @BeforeEach
    public void setup() {
        service = IntegUtil.mock();
    }

    @Test
    public void person_noaddr_shoud_create_and_update_in_db() {
        Model.Person c1_new = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").setEmail("bob@gmail.com").build();
        //valid?
        assertThat(DataQuality.Person.isValidForInsert(c1_new)).isTrue();
        //create
        Model.Person c1_fromdb = service.create(c1_new);

        //update
        final var c1_ToUpdate = Model.Person.newBuilder(c1_fromdb).setLastName("Fork-Updated").build();//.setDirty(true)
        //valid for update
        assertThat(DataQuality.Person.isValidForUpdate(c1_ToUpdate)).isTrue();
        final var c1_Updated = service.update(c1_ToUpdate);
        //logger.info("updated: " + c1_Updated); //TODO document soprint is triggering xxCase() NoSuchMethod.
        //assertThat(c1_Updated).isSameAs(c1_ToUpdate); //TODO document isSameAs is triggering Caused by: java.lang.NoSuchMethodException: muni.model.Model$Person.getCreateTimeCase()
        assertThat(c1_Updated).extracting(Model.Person::getLastName)
                .as(c1_ToUpdate.getLastName());
    }

    @Test
    public void person_changes_must_syc_to_ALL_SUBSYSTEMS_amanda() {
        var amdXref = Model.Xref.newBuilder().setXrefSystemId(Subsys.AMANDA.toString()).build();
        var hansenXref = Model.Xref.newBuilder().setXrefSystemId(Subsys.HANSEN.toString()).build();
        Model.Person c1_new = Model.Person.newBuilder()
                .setFirstName("Bob").setLastName("Fork") //Person info
                .setEmail("bob@gmail.com")
                .putXrefs(amdXref.getXrefSystemId(), amdXref) //This person is needed in subsystem-1
                .putXrefs(hansenXref.getXrefSystemId(), hansenXref) ////This person is needed in subsystem-2
                .build();
        //valid?
        assertThat(DataQuality.Person.isValidForInsert(c1_new)).isTrue();
        //create
        Model.Person c1_fromdb = service.create(c1_new);
        //validate -
        //check person fields
        assertThat(c1_fromdb).extracting(Model.Person::getFirstName, Model.Person::getLastName, Model.Person::getEmail)
                .containsExactly(c1_new.getFirstName(), c1_new.getLastName(), c1_new.getEmail());
        //check xref - Entty is present in subsystem 1 & 2
        var amdXref_fromdb = c1_fromdb.getXrefsMap().get(amdXref.getXrefSystemId());
        assertThat(amdXref_fromdb).isNotNull();
        assertThat(amdXref_fromdb).extracting(Model.Xref::getXrefSystemId, Model.Xref::getId, Model.Xref::getXrefId)
                .containsExactly(amdXref.getXrefSystemId(), c1_fromdb.getId(), amdXref_fromdb.getXrefId());
        assertThat(amdXref_fromdb.getXrefId()).isNotNull();

        //Veroify update
        update_and_verify(c1_fromdb);

    }
    //This method assumes
    private void update_and_verify(Model.Person in) {
        String updatedLastName = in.getLastName() + "Jimmy";
        var person = Model.Person.newBuilder(in).setLastName(updatedLastName).build();
        //update
        var integPerson_fromdb = service.update(person);

        //verify
        //verify lastname in integ obj
        assertThat(integPerson_fromdb).extracting(Model.Person::getLastName).as(updatedLastName);

        //verify lastname propagated to all subsystems
        for (var xref : integPerson_fromdb.getXrefsMap().values()) {
            var xrefPerson = service.getSubsystemPerson(xref).get(); //e.g amandaPerson, hansenPerson...
            assertThat(xrefPerson).as("Verify Person updates in xref system=" + xref.getXrefSystemId())
                    .extracting(Model.Person::getId, Model.Person::getLastName)
                    .containsExactly(xref.getXrefId(), updatedLastName);
        }

    }


}
