package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.assets.status.UserStatus;

public interface CustomUserRepository {

    void suspendUser(long id);
}
