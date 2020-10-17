package access.integ;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;

import java.util.List;
import java.util.Optional;

class IntegServiceImpl implements SubsystemService {

    CRUDDao<Model.Person> personDao;
    CRUDDao<Model.Case> caseDao;

    public IntegServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.Case> caseDao) {
        this.personDao = persDao;
        this.caseDao = caseDao;
    }

    @Override
    public SubsystemDataAccess<Model.Person> person() {
        return buildPersonService();
    }


    @Override
    public SubsystemDataAccess<Model.Case> ccase() {
        return buildCaseService();
    }

    private SubsystemDataAccess<Model.Person> buildPersonService() {
        return new SubsystemDataAccess<Model.Person>() {
            @Override
            public Model.Person save(Model.Person in) {
                long id = personDao.save(in);
                return personDao.getById(id).get();//Once saved, assumed "guaranteed" return
            }

            @Override
            public Optional<Model.Person> get(String id) {
                System.out.println("At integServiceImpl person id=" + id);
                Optional<Model.Person> out = personDao.getById(Long.valueOf(id));
                System.out.println("At integServiceImpl pers=" + out);
                return out;
            }

            @Override
            public List<Model.Person> find(Model.Person person) {
                List<Model.Person> out = personDao.getAll();//TODO change later
                return out;
            }

            @Override
            public List<Model.Person> recent() {
                List<Model.Person> out = personDao.getAll();
                return out;
            }
        };
    }

    private SubsystemDataAccess<Model.Case> buildCaseService() {
        return new SubsystemDataAccess<Model.Case>() {
            @Override
            public Model.Case save(Model.Case aCase) {
                throw new UnsupportedOperationException("Please get it implemented");
            }

            @Override
            public Optional<Model.Case> get(String s) {
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
        };
    }


//
//    //Person ------------------------------------
//    @Override //TODO Priority-1
//    public Model.Person save(Model.Person in) {
//        long id = personDao.save(in);
//        return personDao.getById(id).get();//Once saved, assumed "guaranteed" return
//    }
//
//    @Override
//    public Optional<Model.Person> getPerson(String id) { // Translate integ: string-id to hansen int-id
//        System.out.println("At integServiceImpl person id=" +id);
//        Optional<Model.Person> out = personDao.getById(Long.valueOf(id));
//        System.out.println("At integServiceImpl pers=" +out);
//        return out;
//    }
//    @Override
//    public List<Model.Person> getPersonsRecent() { // Translate integ: string-id to hansen int-id
//        List<Model.Person> out = personDao.getAll();
//        return out;
//    }
//
//    @Override
//    public List<Model.Person> findPerson(Model.Person person) {
//        List<Model.Person> out = new ArrayList();
//        out.add(MockUtil.buildPerson());
//        return out;
//    }
//
//    @Override
//    public Optional<Model.PostalAddress> getPostalAddress(String id) {
//        return Optional.ofNullable(MockUtil.buildAddress());
//    }
//
//    @Override
//    public Model.PostalAddress createPostalAddress(String s) {
//        return MockUtil.buildAddress();
//    }
//    //Case ------------------------------------
//
//
//    @Override //TODO Priority-2
//    public Model.Case save(Model.Case in) {
//        throw new UnsupportedOperationException("Please get it implemented");
//    }
//
//
//    @Override
//    public Optional<Model.Case> getCase(String s) {
//        return null;
//    }
//
//    @Override
//    public List<Model.Case> findCase(Model.Case aCase) {
//        return null;
//    }
}
