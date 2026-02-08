package examples.model.mapper;

import examples.model.dto.request.CreateRoleRequest;
import examples.model.dto.request.UpdateRoleRequest;
import examples.model.entity.Role;
import examples.model.dto.view.RoleView;
import org.mapstruct.Mapper;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.CrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface RoleMapper extends CrudMapper<CreateRoleRequest, UpdateRoleRequest, Role, RoleView> {
}
