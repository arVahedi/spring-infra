package examples.service;

import examples.domain.Credential;
import examples.dto.crud.CredentialDto;
import examples.mapper.CredentialMapper;
import examples.repository.CredentialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.exception.UsernameAlreadyExistsException;
import org.springinfra.service.BaseService;
import org.springinfra.service.DefaultCrudService;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialService extends BaseService implements DefaultCrudService<Credential, CredentialDto> {

    @Getter
    private final CredentialRepository repository;
    @Getter
    private final CredentialMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Credential save(CredentialDto request) {
        //Prevent duplicate usernames
        checkUsernameDuplication(request.getUsername(), Optional.empty());
        return DefaultCrudService.super.save(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Credential update(Long id, CredentialDto request) {
        //Prevent duplicate usernames
        checkUsernameDuplication(request.getUsername(), Optional.of(id));
        return DefaultCrudService.super.update(id, request);
    }

    public Credential findByUsername(String username) {
        return this.repository.findByUsername((username)).orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The username [{0}] does not exist", username)));
    }

    private void checkUsernameDuplication(String username, Optional<Long> excludedId) {
        Optional<Credential> credential = this.repository.findByUsername((username));
        if (credential.isPresent() && (excludedId.isEmpty() || (excludedId.get().longValue() != credential.get().getId()))) {
            throw new UsernameAlreadyExistsException();
        }
    }

}
