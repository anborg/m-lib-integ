package access.integ;
import integ.dao.CaseDao;
import integ.dao.PersonDao;
import mkm.service.SubsystemService;
import muni.model.Model;
import muni.util.MockUtil;

class IntegServiceImpl implements SubsystemService {

    PersonDao personDao;
    CaseDao caseDao ;
    public IntegServiceImpl(PersonDao persDao, CaseDao caseDao){
        this.personDao = persDao;
        this.caseDao = caseDao;
    }

    @Override //TODO Priority-1
    public Model.Person createPerson(Model.Person in) {
        int id = personDao.insert(in);
        return personDao.get(id);
    }
    @Override //TODO Priority-2
    public Model.Case createCase(Model.Case in) {
        throw new UnsupportedOperationException("Please get it implemented");
    }
    @Override
    public Model.Person getPerson(String id) { // Translate integ: string-id to hansen int-id
        //
        return personDao.get(Integer.valueOf(id));
    }

    @Override
    public Model.Person updatePerson(Model.Person in) {
        personDao.update(in);
        return personDao.get(Integer.valueOf(in.getId()));
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
    public Model.PostalAddress updatePostalAddress(String s) {
        return MockUtil.buildAddress();
    }

    @Override
    public Model.Case getCase(String s) {
        return null;
    }
}
