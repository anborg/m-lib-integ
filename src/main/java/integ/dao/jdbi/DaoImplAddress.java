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
    public Long create(Model.Person in) {
        return null;
    }

    @Override
    public Long update(Model.Person in) {
        return null;
    }

    @Override
    public Optional<Model.Person> get(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Model.Person> find(Model.Person in) {
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
    public void delete(Long id) {

    }
}
