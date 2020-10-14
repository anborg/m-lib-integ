package integ.test.service;

import muni.util.DataQuality;
import access.integ.IntegUtil;
import muni.model.Model;
import muni.service.SubsystemService;
import muni.util.MockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestIntegService {

    SubsystemService service;

    @BeforeEach
    public  void setup(){
        service = IntegUtil.inMem();
    }

    public Model.Person createCustomer(){ // Tests create and get
        Model.Person c1 = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").build();
        final var c1_created = service.save(c1);
        return c1_created;
    }

    @Test
    public void crud_person_noaddr_shouldPass(){
        Model.Person c1_new = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").setEmail("bob@gmail.com").build();
        //valid?
        assertThat(DataQuality.Person.isValidForInsert(c1_new)).isTrue();
        //save
        Model.Person c1_fromdb = service.save(c1_new);

        //update
        final var c1_ToUpdate = Model.Person.newBuilder(c1_fromdb).setLastName("Fork-Updated").setDirty(true).build();
        //valid for update
        assertThat(DataQuality.Person.isValidForUpdate(c1_ToUpdate)).isTrue();
        final var c1_Updated = service.save(c1_ToUpdate);
        //System.out.println("updated: " + c1_Updated); //TODO document soprint is triggering xxCase() NoSuchMethod.
        //assertThat(c1_Updated).isSameAs(c1_ToUpdate); //TODO document isSameAs is triggering Caused by: java.lang.NoSuchMethodException: muni.model.Model$Person.getCreateTimeCase()
        assertThat(c1_Updated).extracting(Model.Person::getLastName)
                .containsExactly(c1_ToUpdate.getLastName());
    }


//    @Test
    public void createCase(){
        Model.Case mycase = MockUtil.buildCase();
        final var createdCase = service.save(mycase);
        //System.out.println(createdCase);
    }


}
