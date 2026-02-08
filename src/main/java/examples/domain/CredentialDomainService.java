package examples.domain;

import examples.model.entity.Credential;
import examples.model.entity.CredentialRole;
import examples.model.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springinfra.domain.BaseDomainService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CredentialDomainService extends BaseDomainService {

    private final PasswordEncoder passwordEncoder;

    public void changePassword(Credential credential, String newPassword) {
        var newEncodedPassword = this.passwordEncoder.encode(newPassword);
        credential.setPassword(newEncodedPassword);
    }

    public void updateRoles(Credential credential, List<Role> roles) {
        var existingRolesMap = credential.getRoles()
                .stream()
                .collect(Collectors.toMap(item -> item.getRole().getId(), Function.identity()));

        List<CredentialRole> newRoles = new ArrayList<>();
        roles.forEach(role -> {
            if (existingRolesMap.containsKey(role.getId())) {
                newRoles.add(existingRolesMap.get(role.getId()));
            } else {
                CredentialRole credentialRole = new CredentialRole();
                credentialRole.setRole(role);
                credentialRole.setCredential(credential);
                newRoles.add(credentialRole);
            }
        });

        credential.getRoles().clear();
        credential.getRoles().addAll(newRoles);
    }
}
