package examples.repository;

import org.springframework.stereotype.Repository;
import personal.project.springinfra.database.repository.BaseRepository;
import examples.repository.custom.UserCustomRepository;
import examples.domain.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, UserCustomRepository {
}
