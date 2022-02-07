package examples.converter;

import examples.domain.Role;
import examples.dto.crud.request.RoleDto;
import org.springframework.stereotype.Component;
import springinfra.model.converter.BaseCrudConverter;


@Component
public class RoleCrudConverter extends BaseCrudConverter<RoleDto, Role> {

    @Override
    public Role toEntity(RoleDto dto, Role entity) {
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAuthorities(dto.getAuthorities());

        return entity;
    }
}
