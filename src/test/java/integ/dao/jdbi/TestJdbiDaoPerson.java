package integ.dao.jdbi;

import access.integ.IntegUtil;
import access.integ.Subsys;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SuppressWarnings("deprecation")
public class TestJdbiDaoPerson {
    private static Logger logger = Logger.getLogger(TestJdbiDaoPerson.class.getName());


//    String url3 = "jdbc:h2:mem:testdb;INIT=CREATE SCHEMA IF NOT EXISTS INTEG\\;SET SCHEMA INTEG\\;";

    public Jdbi buildJdbi() {
        Jdbi jdbi = JdbiDbUtil.withDbPlugin(IntegUtil.inmemDS());
        return jdbi;
    }

    @Test
    public void person_CRUD() {

        final var pi_addr = Model.PostalAddress.newBuilder().setStreetNum("111").setStreetName("My street").setCity("Avenly").setPostalCode("L1L0Z0").build();
        final var p1_xref_amanda = Model.Xref.newBuilder().setXrefSystemId(Subsys.AMANDA.toString()).build();
        final var p1 = Model.Person.newBuilder().setFirstName("Alice").setLastName("Doe")
                .setEmail("alice@gmail.com")//To PERSON table
                .setPhone1("12345678")//To PERSON table
                .setPhone2("12345678")//To PERSON table
                //To ADDRES table
                .setAddress(pi_addr)
                .putXrefs(p1_xref_amanda.getXrefSystemId(), p1_xref_amanda)
                .build();
        final var p2 = Model.Person.newBuilder().setFirstName("Clarice").setLastName("Stuck").setEmail("claricee@gmail.com").build();
        final var p3 = Model.Person.newBuilder().setFirstName("Delete").setLastName("Me").setEmail("me@gmail.com").build();
        Jdbi jdbi = buildJdbi();
        //start clean
        jdbi.useExtension(JdbiDaoPerson.class, dao -> dao.deleteAll());
        //when
        List<Model.Person.Builder> personsB = jdbi.withExtension(JdbiDaoPerson.class, persDao -> {
            //insert - address
            Long id_ofP1_a1 = jdbi.withExtension(JdbiDaoPerson.address.class, addrDao -> addrDao.insert(p1.getAddress()));

            logger.info("Addr id id_ofP1_a1=" + id_ofP1_a1);
            //insert - person with address
            Long id_ofP1 = persDao.insert(p1, id_ofP1_a1);
            Long id_ofP1_xamanda = jdbi.withExtension(JdbiDaoPersonXref.class, xrefDao -> xrefDao.insert(id_ofP1, p1_xref_amanda));

            //insert - person without address
            Long id_ofP2 = persDao.insert(p2);
            Long id_ofP3 = persDao.insert(p3);
            logger.info(id_ofP1 + ", " + id_ofP2 + ", " + id_ofP3);
            final Optional<Model.Person.Builder> p1Select = persDao.get(id_ofP1);
            //check - inserted object is in db
            assertThat(p1Select.isPresent()).isTrue();

            final Optional<Model.Person.Builder> p2Select = persDao.get(id_ofP2);
            //check - inserted object is in db
            assertThat(p2Select.isPresent()).isTrue();

            //update - person
            final var p2_updated = p2Select.get().setLastName("Dummy").build();
            long id_p2 = persDao.update(p2_updated);
            //select - person id_ofP1
            final var optp1_fromdb = persDao.get(id_ofP1);
            Model.Person.Builder p1_fromdb = optp1_fromdb.get(); //TODO unpreditable error so adding prev line.
            //verify p1 is in db
            assertThat(p1_fromdb).isNotNull();
            //verify p1 addr is in db
            var p1_addr_fromdb = p1_fromdb.getAddress();
            //check - addr values present
            assertThat(p1_addr_fromdb).isNotNull();
            assertThat(p1_addr_fromdb)
                    .extracting(Model.PostalAddress::getStreetNum, Model.PostalAddress::getStreetName, Model.PostalAddress::getPostalCode, Model.PostalAddress::getCity, Model.PostalAddress::getCountry, Model.PostalAddress::getLat, Model.PostalAddress::getLon)
                    .containsExactly(pi_addr.getStreetNum(), pi_addr.getStreetName(), pi_addr.getPostalCode(), pi_addr.getCity(), pi_addr.getCountry(), pi_addr.getLat(), pi_addr.getLon());
            //check - addr generic inserted obj looks like.
            assertThat(p1_addr_fromdb.getId()).isNotNull().isNotNegative();
            //assertThat(p1_addr_fromdb.getId()).containsOnlyDigits();
            assertThat(p1_addr_fromdb.hasCreateTime()).isTrue();
            assertThat(p1_addr_fromdb.hasUpdateTime()).isTrue();
            //check - person generic inserted obj looks like.
            assertThat(p1_fromdb.getId()).isNotNull().isNotNegative();
            assertThat(p1_fromdb.hasCreateTime()).isTrue();
            assertThat(p1_fromdb.hasUpdateTime()).isTrue();

            //delete - person3
            persDao.delete(id_ofP3);
            //check - deleted obj is null
            Optional<Model.Person.Builder> p3_delselect = persDao.get(id_ofP3);
            assertThat(p3_delselect.isPresent()).isFalse();
            //assertThat(p3_delselect.get()).isNull();//NoSuchElementException: No value present

            return persDao.getAll();


        });//useExtension
        //then
        //asserting - example test at field level
        List<Model.Person> persons = personsB.stream().map(b -> b.build()).collect(Collectors.toList());
        assertThat(persons)
                .extracting(Model.Person::getFirstName, Model.Person::getLastName)
                .containsExactly(
                        tuple(p1.getFirstName(), p1.getLastName()),
                        tuple(p2.getFirstName(), "Dummy")
                );

        //logger.info(persons);

    }
}//testjdbc
