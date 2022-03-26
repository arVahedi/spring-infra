package examples.service;

import examples.converter.CredentialCrudConverter;
import examples.domain.Credential;
import examples.dto.crud.request.CredentialDto;
import examples.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springinfra.exception.UsernameAlreadyExistsException;
import springinfra.model.dto.crud.request.BaseCrudRequest;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialService extends BaseService implements DefaultCrudService<Credential, Long> {

    private final CredentialRepository credentialRepository;
    private final CredentialCrudConverter credentialCrudConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Credential saveOrUpdate(BaseCrudRequest request) {
        //Prevent duplicate usernames
        Optional<Credential> credential = this.credentialRepository.findByUsername(((CredentialDto) request).getUsername());
        if (isUpdateOperation(request)) {
            if (credential.isPresent() && credential.get().getId().longValue() != request.getId().longValue()) {
                throw new UsernameAlreadyExistsException();
            }
        } else if (credential.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        return DefaultCrudService.super.saveOrUpdate(request);
    }

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
