package access.integ;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;
import muni.util.MockUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class IntegServiceImpl implements SubsystemService {

    CRUDDao<Model.Person> personDao;
    CRUDDao<Model.Case> caseDao;

    public IntegServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.Case> caseDao) {
        this.personDao = persDao;
        this.caseDao = caseDao;
    }

    //Person ------------------------------------
    @Override //TODO Priority-1
    public Model.Person save(Model.Person in) {
        long id = personDao.save(in);
        return personDao.getById(id).get();//Once saved, assumed "guaranteed" return
    }

    @Override
    public Optional<Model.Person> getPerson(String id) { // Translate integ: string-id to hansen int-id
        return personDao.getById(Long.valueOf(id));
    }

    @Override
    public List<Model.Person> findPerson(Model.Person person) {
        List<Model.Person> out = new ArrayList();
        out.add(MockUtil.buildPerson());
        return out;
    }

    @Override
    public Optional<Model.PostalAddress> getPostalAddress(String id) {
        return Optional.ofNullable(MockUtil.buildAddress());
    }

    @Override
    public Model.PostalAddress createPostalAddress(String s) {
        return MockUtil.buildAddress();
    }
    //Case ------------------------------------


    @Override //TODO Priority-2
    public Model.Case save(Model.Case in) {
        throw new UnsupportedOperationException("Please get it implemented");
    }


    @Override
    public Optional<Model.Case> getCase(String s) {
        return null;
    }

    @Override
    public List<Model.Case> findCase(Model.Case aCase) {
        return null;
    }
}
