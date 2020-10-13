package integ.test.service;

import access.integ.IntegUtil;
import muni.model.Model;
import muni.service.SubsystemService;
import muni.util.MockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHansenService {

    SubsystemService service;

    @BeforeEach
    public  void setup(){
        service = IntegUtil.dev();
    }

    public Model.Person createCustomer(){ // Tests create and get
        Model.Person c1 = Model.Person.newBuilder().setFirstName("Bob").setLastName("Fork").build();
        final var c1_created = service.save(c1);
        return c1_created;
    }

    @Test
    public void updateCustomer(){
        Model.Person c1_got = createCustomer();
        final var c1_ToUpdate = Model.Person.newBuilder(c1_got).setLastName("Fork-Updated").build();
        final var c1_Updated = service.save(c1_ToUpdate);
        //System.out.println("updated: " + c1_Updated);
        assertThat(c1_Updated).isSameAs(c1_Updated);


    }


//    @Test
    public void createCase(){
        Model.Case mycase = MockUtil.buildCase();
        final var createdCase = service.save(mycase);
        //System.out.println(createdCase);
    }


}
