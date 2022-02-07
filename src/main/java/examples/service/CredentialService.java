package examples.service;

import examples.converter.CredentialCrudConverter;
import examples.domain.Credential;
import examples.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class CredentialService extends BaseService implements DefaultCrudService<Credential, Long> {

    private final CredentialRepository credentialRepository;
    private final CredentialCrudConverter credentialCrudConverter;

    @Override
    public CredentialRepository getRepository() {
        return this.credentialRepository;
    }

    @Override
    public CredentialCrudConverter getCrudConverter() {
        return this.credentialCrudConverter;
    }

    @Override
    public Class<Credential> getGenericDomainClass() {
        return Credential.class;
    }
}
