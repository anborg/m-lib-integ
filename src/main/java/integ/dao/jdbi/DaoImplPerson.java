package integ.dao.jdbi;

import access.integ.DataQuality;
import access.integ.Queries;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
class DaoImplPerson implements CRUDDao<Model.Person> {

    Jdbi jdbi;

    public DaoImplPerson(Jdbi jdbi) {

        this.jdbi = jdbi;
    }


    @Override
    public Optional<Model.Person> get(long id) {
//        return jdbi.withHandle(h -> {
//            Optional<Model.Person> p =
//                    h.createQuery(Queries.sql_person_select_byId)
//                            .bind("id", id)
//                            .map(new MapperPerson())
//                            .findOne();
//            //System.out.println(p);
//            return p;
//        });
        return jdbi.withExtension(JdbiDaoInterface.class, dao ->{
            return dao.get(id);
        });
    }

    @Override
    public Long save(Model.Person in) {
        //check address
        Long addrId = handleAddress(in);

        boolean isValidForInsert = DataQuality.Person.isValidForInsert(in);
        boolean isValidForUpdate = DataQuality.Person.isvalidForUpdate(in);

        if (isValidForInsert) {
            //return (long) jdbi.withExtension(DaoDelegateInterfacePerson.class, dao -> dao.insertNoAddress(in.getFirstName(), in.getLastName(), in.getEmail(), in.getPhone1(), in.getPhone2()));
            return (long) jdbi.withExtension(JdbiDaoInterface.class, dao -> dao.insert(in));
        } else if (isValidForUpdate) {//update
            return jdbi.withExtension(JdbiDaoInterface.class, dao -> dao.update(in.getId(), in.getFirstName(), in.getLastName(), in.getEmail(), in.getPhone1(), in.getPhone2(), addrId));

        } else {
            throw new UnsupportedOperationException("NO DML, why is control coming here? Hint: Did you forget to set dirty? isValidForInsert=" + isValidForInsert + ", isValidForUpdate=" + isValidForUpdate);
        }
//        return null;
    }


    private Long handleAddress(Model.Person in) {
        final var addrIn = in.getAddress();
        if (!in.hasAddress()) {
            System.out.println("No Address to insert");
            return null;
        }
        boolean isValidForInsert = DataQuality.isValidForInsert(addrIn);
        boolean isValidForUpdate = DataQuality.isvalidForUpdate(addrIn);
        if (in.hasAddress()) {
            if (isValidForInsert) {
                return jdbi.withExtension(JdbiDaoInterface.class, dao -> dao.insert(addrIn));

            } else if (isValidForUpdate) {
                return jdbi.withExtension(JdbiDaoInterface.class, dao -> dao.update(addrIn));
            } else {
                throw new UnsupportedOperationException("NO DML to act-on. Why is control coming here? Hint: Did you forget to set dirty? isValidForInsert=" + isValidForInsert + ", isValidForUpdate=" + isValidForUpdate);
            }
        }
        return null;
    }


    @Override
    public List<Model.Person> getAll() {
        return jdbi.withExtension(JdbiDaoInterface.class, JdbiDaoInterface::getAll);
    }

    @Override
    public void deleteAll() {
        jdbi.useExtension(JdbiDaoInterface.class, JdbiDaoInterface::deleteAll);
    }

    @Override
    public void delete(long id) {
        jdbi.useExtension(JdbiDaoInterface.class, dao -> dao.delete(id));
    }

//    public void createTable() {
//        jdbi.useExtension(DaoDelegateInterfacePerson.class, dao -> dao.createTable());
//    }


}
