package examples.service;

import examples.converter.UserCrudConverter;
import examples.domain.User;
import examples.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.model.converter.BaseCrudConverter;
import personal.project.springinfra.service.BaseService;
import personal.project.springinfra.service.DefaultCrudService;

@Service
public class UserService extends BaseService implements DefaultCrudService<User> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCrudConverter userCrudConverter;

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
