package examples.mapper;

import examples.domain.User;
import examples.dto.crud.UserDto;
import org.mapstruct.Mapper;
import org.springinfra.configuration.GlobalMapperConfig;
import org.springinfra.model.mapper.BaseCrudMapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper extends BaseCrudMapper<User, UserDto> {

}
