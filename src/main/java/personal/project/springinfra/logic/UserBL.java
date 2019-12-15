package personal.project.springinfra.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.dao.BaseUserDao;
import personal.project.springinfra.dto.UserDto;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserBL extends BaseBL {

    @Autowired
    private BaseUserDao userDao;

    public User saveOrUpdate(UserDto request) {
        User user = new User();
        if (request.getId() != null && request.getId() > 0) { //This is update operation
            user = this.userDao.findById(request.getId()).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", request.getId())));
            user.setVersion(request.getVersion());
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        return this.userDao.save(user);
    }

    public User find(long id) {
        Optional<User> optional = this.userDao.findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
    }

    public void delete(long id) {
        User user = this.userDao.findById(id).orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
        this.userDao.delete(user);
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }
}
