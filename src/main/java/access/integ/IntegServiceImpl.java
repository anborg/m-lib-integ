package access.integ;

import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;
import muni.util.MockUtil;

class IntegServiceImpl implements SubsystemService {

    CRUDDao<Model.Person> personDao;
    CRUDDao<Model.Case> caseDao;

    public IntegServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.Case> caseDao) {
        this.personDao = persDao;
        this.caseDao = caseDao;
    }

    @Override //TODO Priority-1
    public Model.Person save(Model.Person in) {
        long id = personDao.save(in);
        return personDao.get(id);
    }

    @Override //TODO Priority-2
    public Model.Case save(Model.Case in) {
        throw new UnsupportedOperationException("Please get it implemented");
    }

    @Override
    public Model.Person getPerson(String id) { // Translate integ: string-id to hansen int-id
        return personDao.get(Long.valueOf(id));
    }

    @Override
    public Model.Person findPerson(Model.Person person) {
        return MockUtil.buildPerson();
    }

    @Override
    public Model.PostalAddress getPostalAddress(String s) {
        return MockUtil.buildAddress();
    }

    @Override
    public Model.PostalAddress createPostalAddress(String s) {
        return MockUtil.buildAddress();
    }

    @Override
    public Model.Case getCase(String s) {
        return null;
    }
}
