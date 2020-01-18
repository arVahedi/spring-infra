package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.assets.status.UserStatus;

public class CustomUserRepositoryImpl extends BaseCustomRepository implements CustomUserRepository {

    @Override
    public void updateAllUsersStatus(UserStatus userStatus) {
        getEntityManager().createQuery("update User u set status = :status")
                .setParameter("status", userStatus)
                .executeUpdate();
    }
}
