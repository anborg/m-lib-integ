package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

class DaoImplCase implements CRUDDao<Model.Case> {
    private final Jdbi jdbi;

    public DaoImplCase(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Optional<Model.Case> get(long l) {
        return Optional.empty();
    }

    @Override
    public Long save(Model.Case aCase) {
        return null;
    }

    @Override
    public List<Model.Case> getAll() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void delete(long l) {

    }
}
