package personal.project.springinfra.database.repository;


import org.springframework.stereotype.Repository;
import personal.project.springinfra.model.domain.Credential;

@Repository
public interface CredentialRepository extends BaseRepository<Credential, Long> {
}
