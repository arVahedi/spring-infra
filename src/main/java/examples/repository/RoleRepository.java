package examples.repository;

import examples.domain.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springinfra.database.repository.BaseRepository;

@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends BaseRepository<Role, Integer> {
}
