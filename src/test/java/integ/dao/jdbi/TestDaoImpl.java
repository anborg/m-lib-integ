package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDaoImpl {
    CRUDDao<Model.Person> dao= null;

    @BeforeEach
    public void setup(){dao = new DaoImplPerson(JdbiDbUtil.withDbPlugin(IntegUtil.inmemDS())); }

    @Test
    public void test_jdbi_person_dao() {
        //
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe").setEmail("alice@gmail.com").build();
        Long id = dao.create(p1);
        Optional<Model.Person> opt = dao.get(id);
        assertThat(opt.isPresent()).isTrue();//TODO document, sensitive. if I directly dao.get(id).get() throws exception, But if i use opt.get() it works?! proto3 issue.
        final var p = opt.get();
        assertThat(p).isNotNull();
        assertThat(p)
                .extracting(Model.Person::getId, Model.Person::getFirstName)
                .contains("" + id, "Alice");
        final var email = p.getEmail();
        final var address_id = p.getAddress().getId();
        final var postal_code = p.getAddress().getPostalCode();
        assertThat(Arrays.asList(email, address_id, postal_code))
                .containsExactly("alice@gmail.com", "", "");
    }

    @Test
    public void get_nonexistant_person() {
        Optional<Model.Person> opt = dao.get(9999L);
        System.out.println(opt);
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    public void get_person_withNoaddress() {
        Optional<Model.Person> opt = dao.get(1L);
        assertThat(opt.isPresent()).isFalse();
//        final var p = opt.get();
//        System.out.println(p.toString());
//        assertThat(p.hasAddress()).isFalse();
    }
}
