package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
class DaoImplAddress implements CRUDDao<Model.Person> {
    Jdbi jdbi;

    public DaoImplAddress(Jdbi jdbi) {

        this.jdbi = jdbi;
    }
    @Override
    public Long save(Model.Person in) {
        return null;
    }

    @Override
    public Long update(Model.Person in) {
        return null;
    }

    @Override
    public Optional<Model.Person> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Model.Person> findBySample(Model.Person in) {
        return null;
    }

    @Override
    public void setInactive(Model.Person in) {

    }

    @Override
    public List<Model.Person> getAll() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void delete(long id) {

    }
}
