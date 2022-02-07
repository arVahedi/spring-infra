package examples.service;

import examples.converter.UserCrudConverter;
import examples.domain.User;
import examples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService implements DefaultCrudService<User, Long> {

    private final UserRepository userRepository;
    private final UserCrudConverter userCrudConverter;

    @Override
    public UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public UserCrudConverter getCrudConverter() {
        return this.userCrudConverter;
    }

    @Override
    public Class<User> getGenericDomainClass() {
        return User.class;
    }
}
