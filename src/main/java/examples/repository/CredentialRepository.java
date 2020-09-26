package examples.repository;


import org.springframework.stereotype.Repository;
import examples.domain.Credential;
import personal.project.springinfra.database.repository.BaseRepository;

@Repository
public interface CredentialRepository extends BaseRepository<Credential, Long> {
}
