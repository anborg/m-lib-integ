package integ.dao.jdbi;

import muni.dao.CaseDao;
import org.jdbi.v3.core.Jdbi;

class DaoImplCase implements CaseDao {
    private final Jdbi jdbi;

    public DaoImplCase(Jdbi jdbi) {
        this.jdbi = jdbi;
    }
}
