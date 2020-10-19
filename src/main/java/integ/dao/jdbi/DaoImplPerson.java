package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.util.DataQuality;
import org.jdbi.v3.core.Jdbi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
class DaoImplPerson implements CRUDDao<Model.Person> {

    Jdbi jdbi;

    public DaoImplPerson(Jdbi jdbi) {

        this.jdbi = jdbi;
    }


    @Override
    public Optional<Model.Person> get(Long id) {
//        return jdbi.withHandle(h -> {
//            Optional<Model.Person> p =
//                    h.createQuery(Queries.sql_person_select_byId)
//                            .bind("id", id)
//                            .map(new MapperPerson())
//                            .findOne();
//            //System.out.println(p);
//            return p;
//        });
        return jdbi.withExtension(JdbiDao.person.class, dao -> {
            return dao.get(id);
        });
    }

    @Override
    public Long save(Model.Person in) {
        //check address
        Long addrId = handleAddress(in);

        boolean isValidForInsert = DataQuality.Person.isValidForInsert(in);
        if (isValidForInsert) {
            if(Objects.nonNull(addrId)){
                return (long) jdbi.withExtension(JdbiDao.person.class, dao -> dao.insert(in, addrId));
            }
            return (long) jdbi.withExtension(JdbiDao.person.class, dao -> dao.insert(in));
        } else {
            throw new UnsupportedOperationException("NOT valid for insert, why is control coming here? Hint: Did you forget to set mandatory fields for Person obj? =" + in + "\n================================");
        }
//        return null;
    }

    @Override
    public Long update(Model.Person in) {
        //check address
        Long addrId = handleAddress(in);
        boolean isValidForUpdate = DataQuality.Person.isValidForUpdate(in);

        if (isValidForUpdate) {//update
            if(Objects.nonNull(addrId)){
                return (long) jdbi.withExtension(JdbiDao.person.class, dao -> dao.update(in, addrId));
            }
            return jdbi.withExtension(JdbiDao.person.class, dao -> dao.update(in));
        } else {
            throw new UnsupportedOperationException("NO DML, why is control coming here? Hint: Did you forget to set mandatory fields for Person obj? =" + in + "\n====================================");
        }
//        return null;
    }


    private Long handleAddress(Model.Person in) {
        final var addrIn = in.getAddress();
        if (!in.hasAddress()) {
            System.out.println("No Address to insert");
            return null;
        }
        boolean isValidForInsert = DataQuality.Address.isValidForInsert(addrIn);
        boolean isValidForUpdate = DataQuality.Address.isValidForUpdate(addrIn);
        if (in.hasAddress()) {
            if (isValidForInsert) {
                return jdbi.withExtension(JdbiDao.address.class, dao -> dao.insert(addrIn));

            } else if (isValidForUpdate) {
                return jdbi.withExtension(JdbiDao.address.class, dao -> dao.update(addrIn));
            }else if(DataQuality.Address.isValidForeignKeyRef(addrIn)){
                //check if addr id  exist in db
                // if not exist, throw error!
                Optional<Long> id = jdbi.withHandle(handle -> handle.select("select id from INTEG_ADDRESS where id = CAST(:id as INTEGER)")
                        .bind("id", addrIn.getId())
                        .mapTo(Long.class)
                        .findOne());
                if(id.isEmpty()){
                    throw new RuntimeException("Not found in db: Address # id="+ addrIn.getId()+", MUST be present to use it as reference. Hint: Client providing spurious address-id in request obj?");
                }else{
                    return id.get();
                }
                //jdbi.withExtension(JdbiDao.class, dao -> {
                //    dao.hasAddress(addrIn.getId())
                //});
            }
            else {
                throw new UnsupportedOperationException("NO DML to act-on. Why is control coming here? Hint: Did you forget to set dirty? isValidForInsert=" + isValidForInsert + ", isValidForUpdate=" + isValidForUpdate);
            }
        }
        return null;
    }

    @Override
    public List<Model.Person> findBySample(Model.Person in){
        return Collections.EMPTY_LIST;
    }
    @Override
    public void setInactive(Model.Person id){
        //inactivate
    }
    @Override
    public List<Model.Person> getAll() {
        return jdbi.withExtension(JdbiDao.person.class, JdbiDao.person::getAll);
    }

    @Override
    public void deleteAll() {
        jdbi.useExtension(JdbiDao.person.class, JdbiDao.person::deleteAll);
    }

    @Override
    public void delete(Long id) {
        jdbi.useExtension(JdbiDao.person.class, dao -> dao.delete(id));
    }

//    public void createTable() {
//        jdbi.useExtension(DaoDelegateInterfacePerson.class, dao -> dao.createTable());
//    }

}
