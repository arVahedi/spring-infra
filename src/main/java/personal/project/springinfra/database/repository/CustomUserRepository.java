package personal.project.springinfra.database.repository;

import personal.project.springinfra.assets.status.UserStatus;

public interface CustomUserRepository {

    void updateAllUsersStatus(UserStatus userStatus);
}
