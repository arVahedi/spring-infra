package examples.model.mapper;

import examples.model.dto.request.CreateUserRequest;
import examples.model.dto.request.UpdateUserRequest;
import examples.model.dto.view.UserView;
import examples.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.CrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper extends CrudMapper<CreateUserRequest, UpdateUserRequest, User, UserView> {

    @Mapping(target = "email", expression = "java(request.email().toLowerCase())")
    User toEntity(CreateUserRequest request);

}
