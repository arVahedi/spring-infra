package personal.project.springinfra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.database.repository.UserRepository;
import personal.project.springinfra.model.domain.User;

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
