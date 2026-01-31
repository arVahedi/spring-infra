package examples.mapper;

import examples.domain.Credential;
import examples.domain.CredentialRole;
import examples.domain.Role;
import examples.dto.crud.CredentialDto;
import examples.repository.CredentialRepository;
import examples.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springinfra.assets.Constant;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.BaseCrudMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(config = GlobalMapperConfig.class, uses = UserMapper.class, imports = {Constant.class, StringUtils.class})
public abstract class CredentialMapper implements BaseCrudMapper<Credential, CredentialDto> {

    private RoleService roleService;
    private CredentialRepository credentialRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword", conditionExpression = "java(!Constant.PASSWORD_MASK.equalsIgnoreCase(dto.getPassword()))", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", expression = "java(StringUtils.isNotBlank(dto.getUsername()) ? dto.getUsername().toLowerCase() : dto.getUsername())")
    public abstract Credential toEntity(CredentialDto dto);

    @Override
//    @Mapping(target = "password", ignore = true)
    @Mapping(target = "password", constant = Constant.PASSWORD_MASK)
    public abstract CredentialDto toDto(Credential entity);

    public List<CredentialRole> mapToCredentialRole(List<Long> roleIdList) {
        List<CredentialRole> result = new ArrayList<>();
        roleIdList.forEach(item -> {
            Role role = this.roleService.find(item);
            CredentialRole credentialRole = new CredentialRole();
            credentialRole.setRole(role);
            result.add(credentialRole);
        });
        return result;
    }

    public List<Long> mapToRoleIdList(List<CredentialRole> credentialRoles) {
        return credentialRoles.stream().map(item -> item.getRole().getId()).toList();
    }

    @Named("encodePassword")
    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    @AfterMapping
    public void handleCredentialRoleBidirectional(@MappingTarget Credential credential) {
        // Review added credential roles to use the existing ones to prevent re-insert them as much as possible
        if (!credential.isNew()) {
            List<CredentialRole> mergedCredentialRoles = new ArrayList<>();
            List<CredentialRole> newCredentialRoles = credential.getRoles();
            Optional<Credential> oldCredential = this.credentialRepository.findById(credential.getId());
            if (oldCredential.isPresent()) {
                Map<Long, CredentialRole> existingCredentialRoles = oldCredential.get().getRoles().stream().collect(Collectors.toMap(credentialRole -> credentialRole.getRole().getId(), Function.identity()));
                newCredentialRoles.forEach(item -> mergedCredentialRoles.add(existingCredentialRoles.getOrDefault(item.getRole().getId(), item)));

                credential.setRoles(mergedCredentialRoles);
            }
        }

        // Handle (set) bidirectional relationship of credential and its roles
        credential.getRoles().forEach(credentialRole -> credentialRole.setCredential(credential));
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }
}
