package integ.dao.jdbi;

import muni.dao.CRUDDao;
import muni.model.Model;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

class DaoImplXref implements CRUDDao<Model.Xref> {
    Jdbi jdbi;

    public DaoImplXref(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Long create(Model.Xref xref) {
        return null;
    }

    @Override
    public Long update(Model.Xref xref) {
        return null;
    }

    @Override
    public Optional<Model.Xref> get(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Model.Xref> find(Model.Xref xref) {
        return List.of();
    }

    @Override
    public void setInactive(Model.Xref xref) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Model.Xref> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long aLong) {
        throw new UnsupportedOperationException();
    }
}