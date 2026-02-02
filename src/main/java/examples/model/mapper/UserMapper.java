package examples.model.mapper;

import examples.assets.UserStatus;
import examples.model.dto.UpdateUserRequest;
import examples.model.entity.User;
import examples.model.dto.CreateUserRequest;
import examples.model.view.UserView;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.CrudMapper;
import org.springinfra.model.mapper.DefaultMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper extends CrudMapper<CreateUserRequest, UpdateUserRequest, User, UserView> {


}
