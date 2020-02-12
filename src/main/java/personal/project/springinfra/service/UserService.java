package personal.project.springinfra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.database.repository.UserRepository;
import personal.project.springinfra.dto.crud.UserDto;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseService implements CrudService<User, UserDto> {

    @Autowired
    private UserRepository userRepository;

    /*@Transactional(rollbackFor = Exception.class)
    public User saveOrUpdate(UserDto request) {
        User user;
        if (request.getId() != null && request.getId() > 0) { //This is update operation
            user = this.userRepository.findById(request.getId()).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", request.getId())));
            request.toEntity(user);
        } else {    //This is insert operation
            user = request.toEntity();
            user.setVersion(0L);
        }

        return this.userRepository.save(user);
    }*/

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
        this.userRepository.delete(user);
    }

    public User find(long id) {
        Optional<User> optional = this.userRepository.findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public JpaRepository getRepository() {
        return this.userRepository;
    }
}
