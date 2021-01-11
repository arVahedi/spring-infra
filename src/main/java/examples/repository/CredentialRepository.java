package examples.repository;


import org.springframework.stereotype.Repository;
import examples.domain.Credential;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.database.repository.BaseRepository;

@Repository
@Transactional(readOnly = true)
public interface CredentialRepository extends BaseRepository<Credential, Long> {
}
