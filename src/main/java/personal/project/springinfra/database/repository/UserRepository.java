package personal.project.springinfra.database.repository;

import org.springframework.stereotype.Repository;
import personal.project.springinfra.database.repository.custom.CustomUserRepository;
import personal.project.springinfra.model.domain.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, CustomUserRepository {
}
