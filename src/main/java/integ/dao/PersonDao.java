package integ.dao;
import muni.model.Model;
import java.util.List;
import java.util.Optional;

public interface PersonDao {

    Model.Person get(int id);

    int insert(Model.Person person); //may internally calls get(id)

    void update(Model.Person person);


    @Deprecated //Marked deprecated - not for production
    List<Model.Person> getAll();
    @Deprecated
    void deleteAll();
    @Deprecated
    void delete(int id);
    @Deprecated
    void createTable();
}
