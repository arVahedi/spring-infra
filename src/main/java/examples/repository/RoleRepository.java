package examples.repository;

import examples.model.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.database.repository.BaseRepository;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findFirstByName(String name);
}
