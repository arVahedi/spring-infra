package examples.model.mapper;

import examples.model.dto.request.CreateCredentialRequest;
import examples.model.dto.request.UpdateCredentialRequest;
import examples.model.entity.Credential;
import examples.model.entity.CredentialRole;
import examples.model.dto.view.CredentialView;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springinfra.assets.Constant;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.CrudMapper;

import java.util.List;
import java.util.UUID;

@Mapper(config = GlobalMapperConfig.class, uses = UserMapper.class, imports = {Constant.class, StringUtils.class})
public abstract class CredentialMapper implements CrudMapper<CreateCredentialRequest, UpdateCredentialRequest, Credential, CredentialView> {

    @Override
//    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword", conditionExpression = "java(!Constant.PASSWORD_MASK.equalsIgnoreCase(dto.password()))", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles",  ignore = true)
    @Mapping(target = "username", expression = "java(StringUtils.isNotBlank(dto.username()) ? dto.username().toLowerCase() : dto.username())")
    public abstract Credential toEntity(CreateCredentialRequest dto);

    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles",  ignore = true)
    public abstract void updateEntity(@MappingTarget Credential entity, UpdateCredentialRequest dto);

    @Override
//    @Mapping(target = "password", ignore = true)
    @Mapping(target = "password", constant = Constant.PASSWORD_MASK)
    public abstract CredentialView toView(Credential entity);

    public List<UUID> mapToRoleIdList(List<CredentialRole> credentialRoles) {
        return credentialRoles.stream().map(item -> item.getRole().getPublicId()).toList();
    }

}
