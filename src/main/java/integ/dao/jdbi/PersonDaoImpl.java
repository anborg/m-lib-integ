package integ.dao.jdbi;

import access.integ.IntegUtil;
import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@SuppressWarnings("deprecation")
class PersonDaoImpl implements CRUDDao<Model.Person> {

    Jdbi jdbi;

    public PersonDaoImpl(Jdbi jdbi) {

        this.jdbi = jdbi;
    }


    @Override
    public Model.Person get(long id) {
        return jdbi.withHandle(h -> {
            Model.Person p =
                    h.createQuery(IntegUtil.sql_select_person)
                            .bind("id", id)
                            .map(new PersonMapper())
                            .one();
            System.out.println(p);
            return p;
        });
//        return jdbi.withExtension(DaoDelegateInterfacePerson.class, dao ->{
//            return dao.get(id);
//        });
    }

    @Override
    public long save(Model.Person in) {
        return jdbi.withExtension(DaoDelegateInterfacePerson.class, dao -> dao.save(in));
    }



    @Override
    public List<Model.Person> getAll() {
        return jdbi.withExtension(DaoDelegateInterfacePerson.class, DaoDelegateInterfacePerson::getAll);
    }

    @Override
    public void deleteAll(){
        jdbi.useExtension(DaoDelegateInterfacePerson.class, DaoDelegateInterfacePerson::deleteAll);
    }

    @Override
    public void delete(long id) {
        jdbi.useExtension(DaoDelegateInterfacePerson.class, dao -> dao.delete(id));
    }

    public void createTable() {
        jdbi.useExtension(DaoDelegateInterfacePerson.class, dao -> dao.createTable());
    }


}
