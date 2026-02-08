package examples.domain;

import examples.model.entity.Credential;
import examples.model.entity.CredentialRole;
import examples.model.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CredentialDomainServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void whenUpdatingRoles_thenAddsOrKeepsNewRolesAndRemoveExtraExistingRolesWithoutNulls() {
        CredentialDomainService service = new CredentialDomainService(this.passwordEncoder);

        Role existingRole1 = new Role();
        existingRole1.setId(1L);
        Role existingRole2 = new Role();
        existingRole2.setId(2L);

        Role newRole1 = new Role();
        newRole1.setId(2L);
        Role newRole2 = new Role();
        newRole2.setId(3L);

        Credential credential = new Credential();

        CredentialRole existingCredentialRole1 = new CredentialRole();
        existingCredentialRole1.setCredential(credential);
        existingCredentialRole1.setRole(existingRole1);
        credential.getRoles().add(existingCredentialRole1);

        CredentialRole existingCredentialRole2 = new CredentialRole();
        existingCredentialRole2.setCredential(credential);
        existingCredentialRole2.setRole(existingRole2);
        credential.getRoles().add(existingCredentialRole2);

        service.updateRoles(credential, List.of(newRole1, newRole2));

        assertThat(credential.getRoles()).doesNotContainNull();
        assertThat(credential.getRoles())
                .extracting(item -> item.getRole().getId())
                .contains(newRole1.getId(), newRole2.getId())
                .doesNotContain(existingRole1.getId());
    }
}
