package examples.service;

import examples.domain.Credential;
import examples.dto.crud.request.CredentialDto;
import examples.mapper.CredentialMapper;
import examples.repository.CredentialRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springinfra.exception.UsernameAlreadyExistsException;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialService extends BaseService implements DefaultCrudService<Long, Credential, CredentialDto> {

    private final PasswordEncoder passwordEncoder;
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

    @Override
    public Credential update(Long id, CredentialDto request) {
        //Prevent duplicate usernames
        checkUsernameDuplication(request.getUsername(), Optional.of(id));
        return DefaultCrudService.super.update(id, request);
    }

    public void changePassword(Credential credential, String password) {
        credential.setPassword(this.passwordEncoder.encode(password));
    }

    private void checkUsernameDuplication(String username, Optional<Long> excludedId) {
        Optional<Credential> credential = this.repository.findByUsername((username));
        if (credential.isPresent() && (excludedId.isEmpty() || (excludedId.get().longValue() != credential.get().getId()))) {
            throw new UsernameAlreadyExistsException();
        }
    }

}
