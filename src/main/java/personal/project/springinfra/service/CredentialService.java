package personal.project.springinfra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.CredentialRepository;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.Credential;

import java.util.Optional;

@Service
public class CredentialService extends BaseService implements CrudService<Credential>{

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public JpaRepository getRepository() {
        return this.credentialRepository;
    }

    @Override
    public Class<Credential> getGenericDomainClass() {
        return Credential.class;
    }
}
