package examples.converter;

import examples.domain.User;
import examples.dto.crud.request.UserDto;
import org.springframework.stereotype.Component;
import springinfra.model.converter.BaseCrudConverter;

@Component
public class UserCrudConverter extends BaseCrudConverter<UserDto, User> {

    @Override
    public User toEntity(UserDto dto, User user) {
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhoneNumber());
        user.setStatus(dto.getStatus());
        return user;
    }
}
