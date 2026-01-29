package examples.repository.custom;

import examples.assets.UserStatus;
import examples.domain.User;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.database.repository.custom.BaseCustomRepository;

@Transactional(readOnly = true)
public class UserCustomRepositoryImpl extends BaseCustomRepository<User> implements UserCustomRepository {

    @Override
    public void suspendUser(long id) {
        getEntityManager().createQuery("update User u set u.status = :status where u.id = :id")
                .setParameter("status", UserStatus.SUSPEND)
                .setParameter("id", id)
                .executeUpdate();
    }
}
