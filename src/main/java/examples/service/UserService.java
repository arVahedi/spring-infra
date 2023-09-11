package examples.service;

import examples.domain.User;
import examples.dto.crud.UserDto;
import examples.mapper.UserMapper;
import examples.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService implements DefaultCrudService<Long, User, UserDto> {

    @Getter
    private final UserRepository repository;
    @Getter
    private final UserMapper mapper;

}
