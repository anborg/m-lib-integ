package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPersonDaoImpl {

    @Test
    public void test_jdbi_person_dao() {
        CRUDDao<Model.Person> dao = new PersonDaoImpl(JdbiDbUtil.withDbPlugin(IntegUtil.devDatasource()));
        //
        Model.Person p = dao.get(1L);
        assertThat(p)
                .extracting(Model.Person::getId, Model.Person::getFirstName)
                .contains("1", "Jane");
        final var email = p.getContactChannels().getEmail();
        final var address_id = p.getContactChannels().getPostalAddress().getId();
        final var postal_code = p.getContactChannels().getPostalAddress().getPostalCode();
        assertThat(Arrays.asList(email, address_id, postal_code)).containsExactly("me@gmail.com", "3", "L1L0Z0");
    }
}
