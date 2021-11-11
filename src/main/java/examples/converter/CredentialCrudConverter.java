package examples.converter;

import examples.domain.Credential;
import examples.domain.User;
import examples.dto.crud.request.CredentialDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.project.springinfra.model.converter.BaseCrudConverter;

@Component
@RequiredArgsConstructor
public class CredentialCrudConverter extends BaseCrudConverter<CredentialDto, Credential> {

    private final UserCrudConverter userCrudConverter;

    @Override
    public Credential toEntity(CredentialDto dto, Credential credential) {
        credential.setId(dto.getId());
        credential.setUsername(dto.getUsername());
        credential.changePassword(dto.getPassword());
        if (credential.getUser() == null) {
            credential.setUser(this.userCrudConverter.toEntity(dto.getUser(), User.class));
        } else {
            credential.setUser(this.userCrudConverter.toEntity(dto.getUser(), credential.getUser()));
        }

        return credential;
    }
}
