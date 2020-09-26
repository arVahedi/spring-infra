package examples.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import examples.repository.CredentialRepository;
import examples.domain.Credential;
import personal.project.springinfra.service.BaseService;
import personal.project.springinfra.service.DefaultCrudService;

@Service
public class CredentialService extends BaseService implements DefaultCrudService<Credential> {

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public BaseRepository getRepository() {
        return this.credentialRepository;
    }

    @Override
    public Class<Credential> getGenericDomainClass() {
        return Credential.class;
    }
}
