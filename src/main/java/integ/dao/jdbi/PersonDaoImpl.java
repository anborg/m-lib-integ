package integ.dao.jdbi;

import muni.dao.PersonDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

class PersonDaoImpl implements PersonDao {

    Jdbi jdbi;

    public PersonDaoImpl(Jdbi jdbi){

        this.jdbi =jdbi;
    }

    @Override
    public Model.Person get(int id) {
        return jdbi.withExtension(PersonDaoDelegateInterface.class, dao ->{
            return dao.get(id);
        });
    }
    @Override
    public int insert(Model.Person in) {
        return jdbi.withExtension(PersonDaoDelegateInterface.class, dao ->{
            return dao.insert(in);
        });
    }

    @Override
    public void update(Model.Person in) {
        jdbi.useExtension(PersonDaoDelegateInterface.class, dao ->{
            dao.update(in);
        });
    }

    @Override
    public List<Model.Person> getAll() {
        return jdbi.withExtension(PersonDaoDelegateInterface.class, dao ->{
            return dao.getAll();
        });
    }

    @Override
    public void deleteAll(){
        jdbi.useExtension(PersonDaoDelegateInterface.class, dao ->{
            dao.deleteAll();
        });
    }

    @Override
    public void delete(int id) {
        jdbi.useExtension(PersonDaoDelegateInterface.class, dao ->{
            dao.delete(Integer.valueOf(id));
        });
    }
    public void createTable(){
        jdbi.useExtension(PersonDaoDelegateInterface.class, dao ->{
            dao.createTable();
        });
    }
}
