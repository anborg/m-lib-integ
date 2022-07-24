package access.integ;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * This was an attept to have IntegService to also follow same interface as subsystemservice.
 * However integService needs more fuctions, so this was deprecated.
 */
@Deprecated
class IntegSubsystemServiceImpl implements SubsystemService {
    private static Logger logger = Logger.getLogger(IntegSubsystemServiceImpl.class.getName());
    PersonDA personDA;
    AddressDA addressDA;
    CaseDA caseDA;

    @Deprecated
    public IntegSubsystemServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.Xref> xrefDao, CRUDDao<Model.PostalAddress> addressDao, CRUDDao<Model.Case> caseDao) {
        this.personDA = new PersonDA(persDao, xrefDao);
        this.addressDA = new AddressDA(addressDao);
        this.caseDA = new CaseDA(caseDao);
    }

    @Override
    public SubsystemDataAccess<Model.Person> person() {
        return personDA;
    }

    @Override
    public SubsystemDataAccess<Model.PostalAddress> address() {
        return addressDA;
    }

    @Override
    public SubsystemDataAccess<Model.Case> ccase() {
        return caseDA;
    }



    // Just grouping all Person Data access in one place {save/update/get/find/recent}
    class PersonDA implements SubsystemService.SubsystemDataAccess<Model.Person> {
        CRUDDao<Model.Person> daoPers;
        CRUDDao<Model.Xref> daoXref;

        public PersonDA(CRUDDao<Model.Person> daoPers, CRUDDao<Model.Xref> daoXref) {
            this.daoPers = daoPers;
            this.daoXref = daoXref;
        }

        @Override
        public Model.Person create(Model.Person in) {
            Long id = daoPers.create(in);
            //4. TODO Command-SS to create those accounts - api calls
            //5. TODO Update SS-personId to subbaccounts in INTEG_XREF
            return daoPers.get(id).get();//Once saved, assumed "guaranteed" return
        }

        @Override
        public Model.Person update(Model.Person in) {
            Long id = daoPers.update(in);
            return daoPers.get(id).get();//Once saved, assumed "guaranteed" return
        }

        @Override
        public Optional<Model.Person> get(Long id) {
            logger.info("At integServiceImpl person id=" + id);
            Optional<Model.Person> out = daoPers.get(Long.valueOf(id));
            logger.info("At integServiceImpl pers=" + out);
            return out;
        }

        @Override
        public List<Model.Person> find(Model.Person person) {
            return daoPers.getAll();
        }

        @Override
        public List<Model.Person> recent() {
            return daoPers.getAll();
        }
    }


    // Just grouping all Case Data access in one place {save/update/get/find/recent}
    class AddressDA implements SubsystemService.SubsystemDataAccess<Model.PostalAddress> {
        CRUDDao<Model.PostalAddress> dao;

        public AddressDA(CRUDDao<Model.PostalAddress> dao) {
            this.dao = dao;
        }

        @Override
        public Model.PostalAddress create(Model.PostalAddress in) {
            long id = dao.create(in);
            return dao.get(id).get();//Once saved, assumed "guaranteed" return
        }

        @Override
        public Model.PostalAddress update(Model.PostalAddress in) {
            long id = dao.update(in);
            return dao.get(id).get();//Once saved, assumed "guaranteed" return
        }

        @Override
        public Optional<Model.PostalAddress> get(Long id) {
            logger.info("At integServiceImpl person id=" + id);
            Optional<Model.PostalAddress> out = dao.get(Long.valueOf(id));
            logger.info("At integServiceImpl pers=" + out);
            return out;
        }

        @Override
        public List<Model.PostalAddress> find(Model.PostalAddress in) {
            List<Model.PostalAddress> out = dao.getAll();//TODO change later
            return out;
        }

        @Override
        public List<Model.PostalAddress> recent() {
            return dao.getAll();
        }



    }

    // Just grouping all Case Data access in one place {save/update/get/find/recent}
    class CaseDA implements SubsystemService.SubsystemDataAccess<Model.Case> {
        CRUDDao<Model.Case> dao;

        public CaseDA(CRUDDao<Model.Case> dao) {
        }

        @Override
        public Model.Case create(Model.Case aCase) {
            throw new UnsupportedOperationException("Please get it implemented");
        }

        @Override
        public Model.Case update(Model.Case aCase) {
            throw new UnsupportedOperationException("Please get it implemented");
        }

        @Override
        public Optional<Model.Case> get(Long id) {
            return Optional.empty();
        }

        @Override
        public List<Model.Case> find(Model.Case aCase) {
            return null;
        }

        @Override
        public List<Model.Case> recent() {
            return null;
        }
    }
}//IntegSubsystemServiceImpl


