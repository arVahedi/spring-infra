package examples.repository.custom;

import examples.assets.UserStatus;
import examples.domain.User;
import personal.project.springinfra.database.repository.custom.BaseCustomRepository;

public class UserCustomRepositoryImpl extends BaseCustomRepository<User> implements UserCustomRepository {

    @Override
    public void suspendUser(long id) {
        getEntityManager().createQuery("update User u set u.status = :status where u.id = :id")
                .setParameter("status", UserStatus.SUSPEND)
                .setParameter("id", id)
                .executeUpdate();
    }
}
