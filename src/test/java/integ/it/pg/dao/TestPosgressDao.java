package integ.it.pg.dao;

import integ.dao.PersonDao;
import integ.jdbi.dao.JdbiDbUtil;
import muni.model.Model;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import access.integ.IntegUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SuppressWarnings("deprecation")
public class TestPosgressDao {



    @Test
    public void testDao_CRUD() {
        PersonDao dao = JdbiDbUtil.getDao(IntegUtil.devDatasource(), PersonDao.class);
        //dao.createTable(); Just for one time.
        dao.deleteAll();
        List<Model.Person> listShouldBeEmpty = dao.getAll();
        assertThat(listShouldBeEmpty).isEmpty();
        // insert
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe").build();
        final var p2 = Model.Person.newBuilder().setFirstName("Clarice").setLastName("Stuck").build();
        final var p3 = Model.Person.newBuilder().setFirstName("Delete").setLastName("Me").build();
        int id_ofP1 = dao.insert(p1);
        int id_ofP2 = dao.insert(p2);
        int id_ofP3 = dao.insert(p3);
        // get
        final var p1_got = dao.get(id_ofP1);
        final var p2_got = dao.get(id_ofP2);
        Assertions.assertThat(p1_got)
            .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p1.getFirstName(), p1.getLastName());
        Assertions.assertThat(p2_got)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p2.getFirstName(), p2.getLastName());
        // update
        final var p2_ToUpdate = Model.Person.newBuilder(p2_got).setLastName("Dummy").build();
        dao.update(p2_ToUpdate);
        final var p2_Updated = dao.get(id_ofP2);
        //delete
        dao.delete(id_ofP3);
        assertThat(p2_Updated)
                .extracting(Model.Person::getId,Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p2_got.getId(),p2_got.getFirstName(), "Dummy");


        // get all
        List<Model.Person> listShouldHaveTwo = dao.getAll();
        assertThat(listShouldHaveTwo).hasSize(2);//inserted object is present
        assertThat(listShouldHaveTwo).containsExactly(p1_got, p2_Updated);
        //System.out.println(listShouldHaveTwo);

    }
}




//        then - validate - object level
//        assertThat(persons).containsExactly(p1, p2_updated);
//
//asserting - example test at field level
//        assertThat(persons)
//                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
//                .containsExactly(tuple(p1.getFirstName(), p1.getLastName()),tuple(p2.getFirstName(), "Dummy"));
