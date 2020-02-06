package personal.project.springinfra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.database.repository.UserRepository;
import personal.project.springinfra.dto.UserDto;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public User saveOrUpdate(UserDto request) {
        User user = new User();
        if (request.getId() != null && request.getId() > 0) { //This is update operation
            user = this.userRepository.findById(request.getId()).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", request.getId())));
            user.setVersion(request.getVersion());
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        user.setStatus(request.getStatus());
        return this.userRepository.save(user);
    }

    public User find(long id) {
        Optional<User> optional = this.userRepository.findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
        this.userRepository.delete(user);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
