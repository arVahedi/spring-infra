package examples.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.database.repository.BaseRepository;
import examples.repository.custom.UserCustomRepository;
import examples.domain.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User, Long>, UserCustomRepository {
}
