package examples.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.database.repository.BaseRepository;
import examples.repository.custom.UserCustomRepository;
import examples.model.entity.User;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User>, UserCustomRepository {

    Optional<User> findByEmail(String username);
}
