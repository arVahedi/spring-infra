package personal.project.springinfra.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.project.springinfra.model.domain.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
}
