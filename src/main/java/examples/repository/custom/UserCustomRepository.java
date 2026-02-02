package examples.repository.custom;

import examples.model.entity.User;
import org.springinfra.database.repository.custom.CustomRepository;

public interface UserCustomRepository extends CustomRepository<User> {

    void suspendUser(long id);
}
