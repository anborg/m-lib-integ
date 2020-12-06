package integ.dao.jdbi;

import access.integ.IntegDao;
import access.integ.IntegUtil;
import muni.model.Model;
import muni.util.MockUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestIntegDaoImpl {
//    static IntegDao dao= null;
    static IntegDao dao  = new IntegDaoImpl(JdbiDbUtil.withDbPlugin(IntegUtil.inmemDS()));

//    @BeforeEach
//    public void setup(){dao = new IntegDaoImpl(JdbiDbUtil.withDbPlugin(IntegUtil.inmemDS())); }

    @Test
    public void person_get_id() {
        //
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe").setEmail("alice@gmail.com").build();
        Long id = dao.create(p1);
        Optional<Model.Person> opt = dao.get(id);
        assertThat(opt.isPresent()).isTrue();//TODO document, sensitive. if I directly dao.get(id).get() throws exception, But if i use opt.get() it works?! proto3 issue.
        final var p = opt.get();
        assertThat(p).isNotNull();
        assertThat(p)
                .extracting(Model.Person::getId, Model.Person::getFirstName)
                .contains(id, "Alice");
        final var email = p.getEmail();
        final var address_id = p.getAddress().getId();
        final var postal_code = p.getAddress().getPostalCode();
        assertThat(Arrays.asList(email, address_id, postal_code))
                .containsExactly("alice@gmail.com", 0L, "");
    }

    @Test
    public void person_nonexistant_get_id() {
        Optional<Model.Person> opt = dao.get(9999L);
        System.out.println(opt);
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    public void person_withNoaddress_get() {
        Optional<Model.Person> opt = dao.get(1L);
        assertThat(opt.isPresent()).isTrue();
//        final var p = opt.get();
//        System.out.println(p.toString());
//        assertThat(p.hasAddress()).isFalse();
    }

    @Test
    public void case_create() {
        //
        var reportingPers = MockUtil.buildAnonPerson();
        var eventAddr = MockUtil.buildAddress();
        final var p1 = Model.Case.newBuilder()
                .setTypeId("TREE")
                .setDescription("Tree fell in pedestrian way")
                .setReportedByCustomer(reportingPers)
                .setAddress(eventAddr).build();
        Long id = dao.create(p1);
        Optional<Model.Case> opt = dao.getCase(id);
        assertThat(opt.isPresent()).isTrue();//TODO document, sensitive. if I directly dao.get(id).get() throws exception, But if i use opt.get() it works?! proto3 issue.
        final var out = opt.get();
        assertThat(out).isNotNull();
        assertThat(out)
                .extracting(Model.Case::getId, Model.Case::getTypeId)
                .contains(id, "TREE");
//        final var email = p.getEmail();
//        final var address_id = p.getAddress().getId();
//        final var postal_code = p.getAddress().getPostalCode();
//        assertThat(Arrays.asList(email, address_id, postal_code))
//                .containsExactly("alice@gmail.com", "", "");
    }
}
