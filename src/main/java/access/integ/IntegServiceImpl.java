package access.integ;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;

import java.util.List;
import java.util.Optional;

class IntegServiceImpl implements SubsystemService {
    PersonDA personDA;
    AddressDA addressDA;
    CaseDA caseDA;


    public IntegServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.PostalAddress> addressDao,CRUDDao<Model.Case> caseDao) {
        this.personDA = new PersonDA(persDao);
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
}//IntegServiceImpl


// Just grouping all Person Data access in one place {save/update/get/find/recent}
class  PersonDA implements SubsystemService.SubsystemDataAccess<Model.Person>{
    CRUDDao<Model.Person> dao;
    public PersonDA(CRUDDao<Model.Person> dao){this.dao=dao;}
    @Override
    public Model.Person save(Model.Person in) {
        long id = dao.save(in);
        return dao.get(id).get();//Once saved, assumed "guaranteed" return
    }
    @Override
    public Model.Person update(Model.Person in) {
        long id = dao.update(in);
        return dao.get(id).get();//Once saved, assumed "guaranteed" return
    }
    @Override
    public Optional<Model.Person> get(String id) {
        System.out.println("At integServiceImpl person id=" + id);
        Optional<Model.Person> out = dao.get(Long.valueOf(id));
        System.out.println("At integServiceImpl pers=" + out);
        return out;
    }

    @Override
    public List<Model.Person> find(Model.Person person) {
        List<Model.Person> out = dao.getAll();//TODO change later
        return out;
    }

    @Override
    public List<Model.Person> recent() {
        List<Model.Person> out = dao.getAll();//TODO change later
        return out;
    }
}


// Just grouping all Case Data access in one place {save/update/get/find/recent}
class  AddressDA implements SubsystemService.SubsystemDataAccess<Model.PostalAddress>{
    CRUDDao<Model.PostalAddress> dao;
    public AddressDA(CRUDDao<Model.PostalAddress> dao){this.dao=dao;}
    @Override
    public Model.PostalAddress save(Model.PostalAddress in) {
        long id = dao.save(in);
        return dao.get(id).get();//Once saved, assumed "guaranteed" return
    }
    @Override
    public Model.PostalAddress update(Model.PostalAddress in) {
        long id = dao.update(in);
        return dao.get(id).get();//Once saved, assumed "guaranteed" return
    }
    @Override
    public Optional<Model.PostalAddress> get(String id) {
        System.out.println("At integServiceImpl person id=" + id);
        Optional<Model.PostalAddress> out = dao.get(Long.valueOf(id));
        System.out.println("At integServiceImpl pers=" + out);
        return out;
    }

    @Override
    public List<Model.PostalAddress> find(Model.PostalAddress in) {
        List<Model.PostalAddress> out = dao.getAll();//TODO change later
        return out;
    }

    @Override
    public List<Model.PostalAddress> recent() {
        List<Model.PostalAddress> out = dao.getAll();//TODO change later
        return out;
    }
}
// Just grouping all Case Data access in one place {save/update/get/find/recent}
class  CaseDA implements SubsystemService.SubsystemDataAccess<Model.Case>{
    CRUDDao<Model.Case> dao;
    public CaseDA(CRUDDao<Model.Case> dao){}
    @Override
    public Model.Case save(Model.Case aCase) {
        throw new UnsupportedOperationException("Please get it implemented");
    }
    @Override
    public Model.Case update(Model.Case aCase) {
        throw new UnsupportedOperationException("Please get it implemented");
    }

    @Override
    public Optional<Model.Case> get(String id) {
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