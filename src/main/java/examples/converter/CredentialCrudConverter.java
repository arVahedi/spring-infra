package examples.converter;

import examples.domain.Credential;
import examples.domain.User;
import examples.dto.crud.request.CredentialDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personal.project.springinfra.model.converter.BaseCrudConverter;

@Component
public class CredentialCrudConverter extends BaseCrudConverter<CredentialDto, Credential> {

    @Autowired
    private UserCrudConverter userCrudConverter;

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
