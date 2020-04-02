package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.model.domain.User;

public interface UserCustomRepository extends CustomRepository<User> {

    void suspendUser(long id);
}
