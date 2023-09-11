package examples.mapper;

import examples.domain.Credential;
import examples.domain.CredentialRole;
import examples.domain.Role;
import examples.dto.crud.CredentialDto;
import examples.service.RoleService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinfra.assets.Constant;
import springinfra.configuration.GlobalMapperConfig;
import springinfra.model.mapper.BaseCrudMapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = UserMapper.class, imports = Constant.class)
public abstract class CredentialMapper implements BaseCrudMapper<Credential, CredentialDto> {

    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword", conditionExpression = "java(!Constant.PASSWORD_MASK.equalsIgnoreCase(dto.getPassword()))", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Credential toEntity(CredentialDto dto);

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract CredentialDto toDto(Credential entity);

    public List<CredentialRole> mapToCredentialRole(List<Integer> roleIdList) {
        List<CredentialRole> result = new ArrayList<>();
        roleIdList.forEach(item -> {
            Role role = this.roleService.find(item);
            CredentialRole credentialRole = new CredentialRole();
            credentialRole.setRole(role);
            result.add(credentialRole);
        });
        return result;
    }

    public List<Integer> mapToRoleIdList(List<CredentialRole> credentialRoles) {
        return credentialRoles.stream().map(item -> item.getRole().getId()).toList();
    }

    @Named("encodePassword")
    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    @AfterMapping
    public void handleCredentialRoleBidirectional(@MappingTarget Credential credential) {
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
}
