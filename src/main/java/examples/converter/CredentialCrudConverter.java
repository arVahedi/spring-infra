package examples.converter;

import examples.domain.Credential;
import examples.domain.CredentialRole;
import examples.domain.Role;
import examples.domain.User;
import examples.dto.crud.request.CredentialDto;
import examples.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springinfra.assets.Constant;
import springinfra.model.converter.BaseCrudConverter;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CredentialCrudConverter extends BaseCrudConverter<CredentialDto, Credential> {

    private final UserCrudConverter userCrudConverter;
    private final RoleRepository roleRepository;

    @Override
    public Credential toEntity(CredentialDto dto, Credential credential) {
        credential.setId(dto.getId());
        credential.setUsername(dto.getUsername());
        if (!Constant.PASSWORD_MASK.equalsIgnoreCase(dto.getPassword())) {
            credential.changePassword(dto.getPassword());
        }
        if (credential.getUser() == null) {
            credential.setUser(this.userCrudConverter.toEntity(dto.getUser(), User.class));
        } else {
            this.userCrudConverter.toEntity(dto.getUser(), credential.getUser());
        }

        credential.getRoles().clear();
        dto.getRoles().forEach(roleId -> {
            Optional<Role> role = this.roleRepository.findById(roleId);
            if (role.isPresent()) {
                CredentialRole credentialRole = new CredentialRole();
                credentialRole.setCredential(credential);
                credentialRole.setRole(role.get());
                credential.getRoles().add(credentialRole);
            }
        });

        return credential;
    }
}
