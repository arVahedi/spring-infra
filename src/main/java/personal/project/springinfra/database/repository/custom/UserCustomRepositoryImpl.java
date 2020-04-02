package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.assets.status.UserStatus;
import personal.project.springinfra.model.domain.User;

public class UserCustomRepositoryImpl extends BaseCustomRepository<User> implements UserCustomRepository {

    @Override
    public void suspendUser(long id) {
        getEntityManager().createQuery("update User u set u.status = :status where u.id = :id")
                .setParameter("status", UserStatus.SUSPEND)
                .setParameter("id", id)
                .executeUpdate();
    }
}
