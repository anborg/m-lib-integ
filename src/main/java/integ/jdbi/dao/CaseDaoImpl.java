package integ.jdbi.dao;

import integ.dao.CaseDao;
import org.jdbi.v3.core.Jdbi;

class CaseDaoImpl implements CaseDao {
    private final Jdbi jdbi;

    public CaseDaoImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }
}
