package examples.service;

import examples.converter.CredentialCrudConverter;
import examples.domain.Credential;
import examples.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.model.converter.BaseCrudConverter;
import personal.project.springinfra.service.BaseService;
import personal.project.springinfra.service.DefaultCrudService;

@Service
public class CredentialService extends BaseService implements DefaultCrudService<Credential> {

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialCrudConverter credentialCrudConverter;

    @Override
    public BaseRepository getRepository() {
        return this.credentialRepository;
    }

    @Override
    public BaseCrudConverter getCrudConverter() {
        return this.credentialCrudConverter;
    }

    @Override
    public Class<Credential> getGenericDomainClass() {
        return Credential.class;
    }
}
