package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SuppressWarnings("deprecation")
public class TestDaoDelegateInterfacePerson {


    String url3 = "jdbc:h2:mem:testdb;INIT=CREATE SCHEMA IF NOT EXISTS INTEG\\;SET SCHEMA INTEG\\;";

    public Jdbi buildJdbi() {
        Jdbi jdbi = Jdbi.create(IntegUtil.INMEM_DB_URL);
        jdbi.registerRowMapper(new MapperPerson());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE integ.HSNMOCK_person (id INTEGER PRIMARY KEY, firstname VARCHAR, lastname VARCHAR)");

        });
        return jdbi;
    }

    @Test
    public void testDao_CRUD() {
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe")
                .setEmail("me@gmail.com")//To PERSON table
                .setPhone1("12345678")//To PERSON table
                .setPhone2("12345678")//To PERSON table
                //To ADDRES table
                .setAddress(Model.PostalAddress.newBuilder().setStreetNum("111").setStreetName("My street").setCity("Avenly").setPostalCode("L1L0Z0").build())
                .build();
        final var p2 = Model.Person.newBuilder().setFirstName("Clarice").setLastName("Stuck").build();
        final var p3 = Model.Person.newBuilder().setFirstName("Delete").setLastName("Me").build();
        Jdbi jdbi = buildJdbi();
        //when
        List<Model.Person> persons = jdbi.withExtension(DaoDelegateInterfacePerson.class, dao -> {
            //dao.createTable();
            long id_ofP1 = dao.save(p1);
//            int id_ofP2 = dao.insert(p2);
//            int id_ofP3 = dao.insert(p3);
//            final Model.Person p2Select = dao.get(id_ofP2);
//            final var p2_updated = Model.Person.newBuilder(p2Select).setLastName("Dummy").build();
//            dao.update(p2_updated);
//            //delete
//            dao.delete(id_ofP3);
            return dao.getAll();
        });//useExtension
        //then
        //asserting - example test at field level
        assertThat(persons)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .containsExactly(
                        tuple(p1.getFirstName(), p1.getLastName())
                        //tuple(p2.getFirstName(), "Dummy")
                );
        //System.out.println(persons);
    }
}//testjdbc
