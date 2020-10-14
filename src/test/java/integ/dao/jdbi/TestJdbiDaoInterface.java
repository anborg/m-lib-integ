package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SuppressWarnings("deprecation")
public class TestJdbiDaoInterface {


//    String url3 = "jdbc:h2:mem:testdb;INIT=CREATE SCHEMA IF NOT EXISTS INTEG\\;SET SCHEMA INTEG\\;";

    public Jdbi buildJdbi() {
        Jdbi jdbi = JdbiDbUtil.withDbPlugin(IntegUtil.inmemDatasource());
        return jdbi;
    }

    @Test
    public void testDao_CRUD() {
        final var pi_addr = Model.PostalAddress.newBuilder().setStreetNum("111").setStreetName("My street").setCity("Avenly").setPostalCode("L1L0Z0").build();
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe")
                .setEmail("alice@gmail.com")//To PERSON table
                .setPhone1("12345678")//To PERSON table
                .setPhone2("12345678")//To PERSON table
                //To ADDRES table
                .setAddress(pi_addr)
                .build();
        final var p2 = Model.Person.newBuilder().setFirstName("Clarice").setLastName("Stuck").setEmail("claricee@gmail.com").build();
        final var p3 = Model.Person.newBuilder().setFirstName("Delete").setLastName("Me").setEmail("me@gmail.com").build();
        Jdbi jdbi = buildJdbi();
        //start clean
        jdbi.useExtension(JdbiDaoInterface.class, dao -> dao.deleteAll());
        //when
        List<Model.Person> persons = jdbi.withExtension(JdbiDaoInterface.class, dao -> {
            long id_ofP1_a1 = dao.insert(p1.getAddress());
            System.out.println("Addr id id_ofP1_a1="+id_ofP1_a1);
            long id_ofP1 = dao.insert(p1,id_ofP1_a1);
            long id_ofP2 = dao.insert(p2);
            long id_ofP3 = dao.insert(p3);
            System.out.println(id_ofP1 + ", " + id_ofP2 + ", " + id_ofP3);
            final Optional<Model.Person> p2Select = dao.getPersonById(id_ofP2);
            assertThat(p2Select.isPresent()).isTrue();
            final var p2_updated = Model.Person.newBuilder(p2Select.get()).setLastName("Dummy").build();
            long id_p2 = dao.updateNoAddress(p2_updated);
//          //delete
            dao.delete(id_ofP3);
            //id_ofP1
            final var optp1_fromdb = dao.getPersonById(id_ofP1);
            Model.Person p1_fromdb = optp1_fromdb.get(); //TODO unpreditable error
            var p1_addr_fromdb = p1_fromdb.getAddress();
            //verify address
            assertThat(p1_fromdb).isNotNull();
            assertThat(p1_addr_fromdb)
                    .extracting(Model.PostalAddress::getStreetNum, Model.PostalAddress::getStreetName,Model.PostalAddress::getPostalCode, Model.PostalAddress::getCity, Model.PostalAddress::getCountry, Model.PostalAddress::getLat, Model.PostalAddress::getLon )
                    .containsExactly(pi_addr.getStreetNum(), pi_addr.getStreetName(),pi_addr.getPostalCode(), pi_addr.getCity(), pi_addr.getCountry(), pi_addr.getLat(), pi_addr.getLon() );
            //True for a inserted object- PostalAddress
            assertThat(p1_addr_fromdb.getId()).isNotEmpty();
            assertThat(p1_addr_fromdb.getId()).isNotNull();
            assertThat(p1_addr_fromdb.getId()).containsOnlyDigits();
            assertThat(p1_addr_fromdb.hasCreateTime()).isTrue();
            assertThat(p1_addr_fromdb.hasUpdateTime()).isTrue();
            //True for a inserted object - Person
            assertThat(p1_fromdb.getId()).isNotEmpty();
            assertThat(p1_fromdb.getId()).isNotNull();
            assertThat(p1_fromdb.getId()).containsOnlyDigits();
            assertThat(p1_fromdb.hasCreateTime()).isTrue();
            assertThat(p1_fromdb.hasUpdateTime()).isTrue();
            return dao.getAll();
        });//useExtension
        //then
        //asserting - example test at field level
        assertThat(persons)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .containsExactly(
                        tuple(p1.getFirstName(), p1.getLastName()),
                        tuple(p2.getFirstName(), "Dummy")
                );

        //System.out.println(persons);
    }
}//testjdbc
