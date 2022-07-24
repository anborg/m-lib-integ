package integ.dao.jdbi;

import access.integ.IntegDao;
import access.integ.Subsys;
import muni.model.Model;
import muni.util.DataQuality;
import muni.util.ProtoUtil;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class IntegDaoImpl implements IntegDao {
    private Logger logger = Logger.getLogger(IntegDaoImpl.class.getName());
    Jdbi jdbi;

    public IntegDaoImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    // ---- Helper functions ---
    //update xref in Person object //Note: PersonXref are inserted in IntegServiceImpl, and deactivated in other mechanism
    // NO UPDATE for XREF_PERSON !
    /*
    private void forPersonInsertOrUpdateXref(final Long masterPersonId, Model.Person in) {
        Stream<Model.Xref> insertable = in.getXrefAccountsMap().values().stream().filter(x -> isInsertable(x));
        Stream<Model.Xref> updatable = in.getXrefAccountsMap().values().stream().filter(x -> !isInsertable(x));

        insertable.forEach(xref -> jdbi.withExtension(JdbiDaoPerson.xref.class, dao -> dao.insert(masterPersonId, xref)));
        updatable.forEach(xref -> jdbi.withExtension(JdbiDaoPerson.xref.class, dao -> dao.update(masterPersonId, xref)));

    }*/
    private static boolean isInsertable(Model.Xref in) {
        // no xrefId filled, so new link to subsystem, so insert.
        return !in.hasXrefId(); // not insertable

    }

    /**
     * NEW-Person, No-id,
     * - if Address present, insert => addr_id
     * - insert person core fields fn ln,email,phone, addr_id => pers_id
     * - DO NOT call Subsystem.person.create(pers_id, xref) -- will be handled in ServiceLayer, Reason: If a subsystem is unavailable, subsys-Error must be ignored just for that subsystem.
     *
     * @param in person
     * @return peronId
     */
    @Override
    public Long create(Model.Person in) {
        boolean isValidForInsert = DataQuality.Person.isValidForInsert(in);
        if (!isValidForInsert)
            throw new UnsupportedOperationException("NOT valid for insert, why is control coming here? Hint: Did you forget to set mandatory fields for Person obj? =" + in + "\n================================");
        //1. Save the address.
        Long addrId = forPersonInsertOrUpdateAddress(in);

        //2. Save person details - FN, LN, email
        Long personId ;
        if (Objects.nonNull(addrId)) { // addr inserted
            personId = jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.insert(in, addrId));
        } else {
            personId = jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.insert(in));
        }

        //3. Save intentions on SS-accounts in INTEG_XREF
        //forPersonInsertOrUpdateXref(personId, in); //XREF are inserted in integService,
        return personId;
//        var opt= get(""+personId);
//        var pCreated = opt.isPresent()? opt.get() : null;
//        return pCreated;
    }

    @Override
    public Optional<Model.Person> get(Long id) {
        logger.info("Dao get person for id=" + id);
//        Long idLong = Long.valueOf(id);
        var optPerson = jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.get(id));
        return optPerson.map(b -> b.build());
    }

    public List<Model.Person> getRecentPersons() {
        return jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.getAll()).stream().map(b -> b.build()).collect(Collectors.toList());
    }

    @Override
    public Model.Person update(Model.Person in) {
        //check address
        Long addrId = forPersonInsertOrUpdateAddress(in);//TODO watch : may add 'duplicate' address
        boolean isValidForUpdate = DataQuality.Person.isValidForUpdate(in);
        Long personId = null;

        if (isValidForUpdate) {//update
            if (Objects.nonNull(addrId)) {
                personId = jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.update(in, addrId));
            } else {
                personId = jdbi.withExtension(JdbiDaoPerson.class, dao -> dao.update(in));
            }
        } else {
            throw new UnsupportedOperationException("NO DML, why is control coming here? Hint: Did you forget to set mandatory fields for Person obj? =" + in + "\n====================================");
        }
        //forPersonInsertOrUpdateXref(personId, in);
        logger.info("At integdaoimpl pid="+in.getId());
        var opt = get(in.getId());
        return opt.isPresent() ? opt.get() : null;

    }

    //At Service layer Call syubsys.create(Person), take the id, create an Xref object
    // IF success: call this dao method to record the creation.
    // if fail: call this dao, it records INTENT (i.e null xerf.personId ). Intent may be fulfilled by some other mechanism, e.g background thread.
    @Override
    public Model.Person recordIntentXref(Model.Person in, Subsys subsysType) {
        if (in.hasId() && Objects.nonNull(subsysType)) {
            jdbi.withExtension(JdbiDaoPersonXref.class, dao -> dao.recordIntentPersonXref(Long.valueOf(in.getId()), subsysType.toString()));
        }
        return in;
    }

    @Override
    public Long create(Model.Xref in) {
        if (in.hasId() && in.hasXrefSystemId()){
            return jdbi.withExtension(JdbiDaoPersonXref.class, dao -> dao.insert(in));
        }else{
            throw new RuntimeException("Invalid Xref to save, must have master_id & subsystem_id");
        }
    }

    // ---- Case  ---
    @Override
    public Optional<Model.Case> getCase(Long id) {
        logger.info("Dao get case for id=" + id);
        Long idLong = Long.valueOf(id);
        var optPerson = jdbi.withExtension(JdbiDaoCase.class, dao -> dao.get(idLong));
        return optPerson.map(b -> b.build());
    }

    public List<Model.Case> getRecentCases() {
        return jdbi.withExtension(JdbiDaoCase.class, dao -> dao.getAll()).stream().map(b -> b.build()).collect(Collectors.toList());
    }

    @Override
    public Long create(Model.Case in) {
        DataQuality.Case.isValidForInsert(in);
        //1. NO Save the address - assume address already in db
        //Long addrId = forCaseInsertOrUpdateAddress(in);

        //2. Save Case details - FN, LN, email
        Long id = jdbi.withExtension(JdbiDaoCase.class, dao -> dao.insert(in));

        //3. Save intentions on SS-accounts in INTEG_XREF
        //forPersonInsertOrUpdateXref(personId, in); //XREF are inserted in integService,
        return id;

    }

    @Override
    public Model.Case recordIntentXref(Model.Case in, Subsys subsysType) {
        return null;
    }

    @Override
    public Model.Case update(Model.Case in) {
        //check address
        Long addrId = null;//forPersonInsertOrUpdateAddress(in);//TODO watch : may add 'duplicate' address

        DataQuality.Case.isValidForUpdate(in);

        Long id;
        if (Objects.nonNull(addrId)) {
            //id = jdbi.withExtension(   JdbiDaoCase.class, dao -> dao.update(in, addrId));
        } else {
            logger.info("before case insert json=\n" + ProtoUtil.toJson(in));
            id = jdbi.withExtension(JdbiDaoCase.class, dao -> dao.update(in));
        }

        logger.info("At integdaoimpl case=" + in.getId());
        var opt = getCase(in.getId());
        return opt.isPresent() ? opt.get() : null;
    }

    private Long forPersonInsertOrUpdateAddress(Model.Person in) {
        final var addrIn = in.getAddress();
        if (!in.hasAddress()) {
            logger.info("No Address to insert");
            return null;
        }
        boolean isValidForInsert = DataQuality.Address.isValidForInsert(addrIn);
        boolean isValidForUpdate = DataQuality.Address.isValidForUpdate(addrIn);
        if (in.hasAddress()) {
            if (isValidForInsert) {
                return jdbi.withExtension(JdbiDaoPerson.address.class, dao -> dao.insert(addrIn));

            } else if (isValidForUpdate) {
                return jdbi.withExtension(JdbiDaoPerson.address.class, dao -> dao.update(addrIn));
            } else if (DataQuality.Address.isValidForeignKeyRef(addrIn)) {
                //check if addr id  exist in db
                // if not exist, throw error!
                Optional<Long> id = jdbi.withHandle(handle -> handle.select("select id from INTEG_ADDRESS where id = CAST(:id as INTEGER)")
                        .bind("id", addrIn.getId())
                        .mapTo(Long.class)
                        .findOne());
                if (id.isEmpty()) {
                    throw new RuntimeException("Not found in db: Address # id=" + addrIn.getId() + ", MUST be present to use it as reference. Hint: Client providing spurious address-id in request obj?");
                } else {
                    return id.get();
                }
                //jdbi.withExtension(JdbiDaoPerson.class, dao -> {
                //    dao.hasAddress(addrIn.getId())
                //});
            } else {
                throw new UnsupportedOperationException("NO DML to act-on. Why is control coming here? Hint: Did you forget to set dirty? isValidForInsert=" + isValidForInsert + ", isValidForUpdate=" + isValidForUpdate);
            }
        }
        return null;
    }
}