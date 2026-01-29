package examples.mapper;

import examples.domain.Role;
import examples.dto.crud.RoleDto;
import org.mapstruct.Mapper;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.BaseCrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface RoleMapper extends BaseCrudMapper<Role, RoleDto> {
}
