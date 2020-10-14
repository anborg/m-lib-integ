package integ.it.pg.dao;

import access.integ.IntegUtil;
import integ.dao.jdbi.JdbiDbUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SuppressWarnings("deprecation")
public class TestPersonDaoImpl {


    @Test
    public void testDao_CRUD() {
        CRUDDao<Model.Person> dao = JdbiDbUtil.getDao(IntegUtil.inmemDatasource(), Model.Person.class);
        //dao.createTable(); //Just for one time.
        dao.deleteAll();
        List<Model.Person> listShouldBeEmpty = dao.getAll();
        assertThat(listShouldBeEmpty).isEmpty();
        // insert
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe").setEmail("alice@gmail.com").build();
        final var p2 = Model.Person.newBuilder().setFirstName("Clarice").setLastName("Stuck").setEmail("clarice@gmail.com").build();
        final var p3 = Model.Person.newBuilder().setFirstName("Delete").setLastName("Me").setEmail("delete@gmail.com").build();
        Long id_ofP1 = dao.save(p1);
        Long id_ofP2 = dao.save(p2);
        Long id_ofP3 = dao.save(p3);
        System.out.println(id_ofP1 + ", " + id_ofP2 + ", " + id_ofP3);
        // get
        final var p1_got = dao.getById(id_ofP1).get();
        final var p2_got = dao.getById(id_ofP2).get();
        Assertions.assertThat(p1_got)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p1.getFirstName(), p1.getLastName());
        Assertions.assertThat(p2_got)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p2.getFirstName(), p2.getLastName());
        // update
        final var p2_ToUpdate = Model.Person.newBuilder(p2_got).setLastName("Stuck-Updated").setDirty(true).build();
        dao.save(p2_ToUpdate);
        final var p2_Updated = dao.getById(id_ofP2).get();
        //delete
        dao.delete(id_ofP3);
        assertThat(p2_Updated)
                .extracting(Model.Person::getId,Model.Person::getFirstName, Model.Person::getLastName)
                .contains(p2_got.getId(),p2_got.getFirstName(), "Stuck-Updated");


        // get all
        List<Model.Person> listShouldHaveTwo = dao.getAll();
        assertThat(listShouldHaveTwo).hasSize(2);//inserted object is present
        //assertThat(listShouldHaveTwo).containsExactly(p1_got, p2_Updated); //TODO document : triggers NoSuchMethodException: muni.model.Model$Person.getCreateTimeCase()
        assertThat(listShouldHaveTwo)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .contains(tuple(p1.getFirstName(), p1.getLastName())
                        ,tuple(p2_ToUpdate.getFirstName(), p2_ToUpdate.getLastName()));
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
