package examples.service;

import examples.domain.CredentialDomainService;
import examples.model.dto.request.CreateCredentialRequest;
import examples.model.dto.view.CredentialView;
import examples.model.entity.Credential;
import examples.model.entity.Role;
import examples.model.mapper.CredentialMapper;
import examples.repository.CredentialRepository;
import examples.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.exception.UsernameAlreadyExistsException;
import org.springinfra.model.dto.request.PropertyBagRequest;
import org.springinfra.service.BaseService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CredentialService extends BaseService {

    private final CredentialDomainService credentialDomainService;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final CredentialMapper credentialMapper;

    public CredentialView save(CreateCredentialRequest request) {
        //Prevent duplicate usernames
        checkUsernameUniqueness(request.username());
        var credential = this.credentialMapper.toEntity(request);
        this.credentialDomainService.changePassword(credential, request.password());
        List<Role> roleEntities = new ArrayList<>();
        request.roles().forEach(rolePId -> {
            Role role = this.roleRepository.findByPublicId(rolePId)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", rolePId)));
            roleEntities.add(role);
        });
        this.credentialDomainService.updateRoles(credential, roleEntities);

        credential = this.credentialRepository.save(credential);
        return this.credentialMapper.toView(credential);
    }

    public CredentialView patch(UUID publicId, PropertyBagRequest propertyBagRequest) {
        var credential = this.credentialRepository.findByPublicId(publicId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", publicId)));

        if (propertyBagRequest.getProperties().containsKey("password")) {
            this.credentialDomainService.changePassword(credential, (String) propertyBagRequest.getProperty("password"));
        }

        if (propertyBagRequest.getProperties().containsKey("roles")) {
            List<Role> roleEntities = new ArrayList<>();
            ((List<?>) propertyBagRequest.getProperty("roles")).stream()
                    .map(item -> UUID.fromString(item.toString()))
                    .forEach(rolePublicId -> {
                        Role role = this.roleRepository.findByPublicId(rolePublicId)
                                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", publicId)));
                        roleEntities.add(role);
                    });

            this.credentialDomainService.updateRoles(credential, roleEntities);
        }

        credential = this.credentialRepository.save(credential);
        return this.credentialMapper.toView(credential);
    }

    public CredentialView findById(UUID publicId) {
        var credential = this.credentialRepository.findByPublicId(publicId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", publicId)));
        return this.credentialMapper.toView(credential);
    }

    public void delete(UUID publicId) {
        this.credentialRepository.deleteByPublicId(publicId);
    }

    public List<CredentialView> findAll(Pageable pageable) {
        var credentials = this.credentialRepository.findAll(pageable);
        return this.credentialMapper.toViews(credentials.getContent());
    }

    public CredentialView findByUsername(String username) {
        var credential = this.credentialRepository.findByUsername((username))
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The username [{0}] does not exist", username)));
        return this.credentialMapper.toView(credential);
    }

    private void checkUsernameUniqueness(String username) {
        Optional<Credential> credential = this.credentialRepository.findByUsername((username.toLowerCase()));
        if (credential.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
    }

}
