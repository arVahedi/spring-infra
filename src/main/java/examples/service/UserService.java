package examples.service;

import examples.converter.UserCrudConverter;
import examples.domain.User;
import examples.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.model.converter.BaseCrudConverter;
import personal.project.springinfra.service.BaseService;
import personal.project.springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService implements DefaultCrudService<User> {

    private final UserRepository userRepository;
    private final UserCrudConverter userCrudConverter;

    @Override
    public BaseRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public BaseCrudConverter getCrudConverter() {
        return this.userCrudConverter;
    }

    @Override
    public Class<User> getGenericDomainClass() {
        return User.class;
    }
}
