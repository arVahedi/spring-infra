package examples.mapper;

import examples.domain.Role;
import examples.dto.crud.RoleDto;
import org.mapstruct.Mapper;
import springinfra.configuration.GlobalMapperConfig;
import springinfra.model.mapper.BaseCrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface RoleMapper extends BaseCrudMapper<Role, RoleDto> {
}
