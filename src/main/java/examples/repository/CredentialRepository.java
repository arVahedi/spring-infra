package examples.repository;


import examples.domain.Credential;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.database.repository.BaseRepository;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CredentialRepository extends BaseRepository<Credential, Long> {

    Optional<Credential> findByUsername(String username);
}
