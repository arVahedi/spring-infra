package examples.service;

import examples.assets.UserStatus;
import examples.domain.Credential;
import examples.domain.CredentialRole;
import examples.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service(DefaultUserDetailsService.BEAN_NAME)
@RequiredArgsConstructor
public class DefaultUserDetailsService extends BaseService implements UserDetailsService {
    public static final String BEAN_NAME = "defaultUserDetailsService";

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Credential> credential = this.credentialRepository.findByUsername(username);

        if (credential.isPresent()) {
            return new org.springframework.security.core.userdetails.User(credential.get().getUsername(),
                    credential.get().getPassword(),
                    credential.get().getUser().getStatus() == UserStatus.ACTIVE,
                    true,
                    true,
                    true,   //todo: check for lock accounts to prevent brute-force attacks
                    getAuthorities(credential.get().getRoles()));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<CredentialRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (CredentialRole credentialRole : roles) {
            credentialRole.getRole().getAuthorities().stream()
                    .map(authorityType -> new SimpleGrantedAuthority(authorityType.getValue()))
                    .forEach(authorities::add);
        }

        return authorities;
    }
}
