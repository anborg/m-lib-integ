package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.util.DataQuality;
import org.jdbi.v3.core.Jdbi;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
class DaoImplPerson implements CRUDDao<Model.Person> {

    Jdbi jdbi;

    public DaoImplPerson(Jdbi jdbi) { this.jdbi = jdbi; }


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
        var optPerson= jdbi.withExtension(JdbiDao.person.class, dao -> { return dao.get(id); });
        List<Model.Xref> xref = optPerson.isPresent()? jdbi.withExtension(JdbiDao.xref.class, dao -> {return dao.getXrefPerson(id);}) : List.of();
        if(xref.isEmpty()){
            return optPerson;
        }else{
            var pBuilder = Model.Person.newBuilder(optPerson.get());
            xref.forEach(x -> pBuilder.putXrefAccounts(x.getXrefSubsysId(), x));
            return Optional.of(pBuilder.build());
        }
    }

    @Override
    public Long create(Model.Person in) {
        boolean isValidForInsert = DataQuality.Person.isValidForInsert(in);
        if(!isValidForInsert) throw new UnsupportedOperationException("NOT valid for insert, why is control coming here? Hint: Did you forget to set mandatory fields for Person obj? =" + in + "\n================================");
        //1. Save the address.
        Long addrId = handleAddress(in);
        //2. Save person details
        Long personId = null;
        if(Objects.nonNull(addrId)){
            personId =  jdbi.withExtension(JdbiDao.person.class, dao -> dao.insert(in, addrId));
        }else{
            personId =jdbi.withExtension(JdbiDao.person.class, dao -> dao.insert(in));
        }
        //3. Save intentions on SS-accounts in INTEG_XREF
        handleXref(personId, in);
        return personId;
    }
    private void handleXref(final Long personId, Model.Person in){
        in.getXrefAccountsMap().values().forEach( xref -> {
            jdbi.withExtension(JdbiDao.xref.class, dao -> dao.insert(personId, xref));
        });
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
    public List<Model.Person> find(Model.Person in){
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
