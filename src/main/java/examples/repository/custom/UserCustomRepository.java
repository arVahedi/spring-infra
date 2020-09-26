package examples.repository.custom;

import examples.domain.User;
import personal.project.springinfra.database.repository.custom.CustomRepository;

public interface UserCustomRepository extends CustomRepository<User> {

    void suspendUser(long id);
}
