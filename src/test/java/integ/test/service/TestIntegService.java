package integ.test.service;

import access.integ.IntegUtil;
import muni.model.Model;
import muni.service.SubsystemService;
import muni.util.DataQuality;
import muni.util.MockUtil;

import static org.assertj.core.api.Assertions.assertThat;

@Deprecated
public class TestIntegService {

    SubsystemService service;


    //@BeforeEach
    public void setup() {
        service = IntegUtil.inMem();
    }

    public Model.Person createCustomer() { // Tests create and get
        Model.Person c1 = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").build();
        return service.person().create(c1);
    }

    //@Test
    public void crud_person_noaddr_shouldPass(){
        Model.Person c1_new = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").setEmail("bob@gmail.com").build();
        //valid?
        assertThat(DataQuality.Person.isValidForInsert(c1_new)).isTrue();
        //create
        Model.Person c1_fromdb = service.person().create(c1_new);

        //update
        final var c1_ToUpdate = Model.Person.newBuilder(c1_fromdb).setLastName("Fork-Updated").build();//.setDirty(true)
        //valid for update
        assertThat(DataQuality.Person.isValidForUpdate(c1_ToUpdate)).isTrue();
        final var c1_Updated = service.person().update(c1_ToUpdate);
        //System.out.println("updated: " + c1_Updated); //TODO document soprint is triggering xxCase() NoSuchMethod.
        //assertThat(c1_Updated).isSameAs(c1_ToUpdate); //TODO document isSameAs is triggering Caused by: java.lang.NoSuchMethodException: muni.model.Model$Person.getCreateTimeCase()
        assertThat(c1_Updated).extracting(Model.Person::getLastName)
                .containsExactly(c1_ToUpdate.getLastName());
    }

    //@Test
    public void create_person_for_ssAmanda(){
        var amdXref = Model.Xref.newBuilder().setXrefSystemId("AMANDA").build();
        Model.Person c1_new = Model.Person.newBuilder()
                .setFirstName("Bob").setLastName("Fork")
                .setEmail("bob@gmail.com")
                .putXrefAccounts(amdXref.getXrefSystemId(), amdXref)
                //.putXrefAccounts("HANSEN", null)
                .build();
        //valid?
        assertThat(DataQuality.Person.isValidForInsert(c1_new)).isTrue();
        //create
        Model.Person c1_fromdb = service.person().create(c1_new);
        //validate -
        assertThat(c1_fromdb).extracting(Model.Person::getFirstName, Model.Person::getLastName, Model.Person::getEmail)
                .containsExactly(c1_new.getFirstName(), c1_new.getLastName(), c1_new.getEmail());
        var amdXref_fromdb = c1_fromdb.getXrefAccountsMap().get(amdXref.getXrefSystemId());
        assertThat(amdXref_fromdb).isNotNull();

        assertThat(amdXref_fromdb).extracting(Model.Xref::getXrefSystemId)
                .containsExactly(amdXref.getXrefSystemId());
        assertThat(amdXref_fromdb.getXrefId()).isNotNull();
    }


    //    @Test
    public void createCase(){
        Model.Case mycase = MockUtil.buildCase();
        final var createdCase = service.ccase().create(mycase);
        //System.out.println(createdCase);
    }
}
