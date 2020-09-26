package examples.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import examples.repository.UserRepository;
import examples.domain.User;
import personal.project.springinfra.service.BaseService;
import personal.project.springinfra.service.DefaultCrudService;

@Service
public class UserService extends BaseService implements DefaultCrudService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public BaseRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public Class<User> getGenericDomainClass() {
        return User.class;
    }
}
