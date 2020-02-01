package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.assets.status.UserStatus;
import personal.project.springinfra.model.domain.User;

public class CustomUserRepositoryImpl extends BaseCustomRepository<User, Long> implements CustomUserRepository {

    @Override
    public void updateAllUsersStatus(UserStatus userStatus) {
        getEntityManager().createQuery("update User u set u.status = :status")
                .setParameter("status", userStatus)
                .executeUpdate();
    }
}
