package examples.mapper;

import examples.domain.User;
import examples.dto.crud.request.UserDto;
import org.mapstruct.Mapper;
import springinfra.configuration.GlobalMapperConfig;
import springinfra.model.mapper.BaseCrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper extends BaseCrudMapper<User, UserDto> {

}
