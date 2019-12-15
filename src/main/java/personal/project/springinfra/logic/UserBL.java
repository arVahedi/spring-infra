package personal.project.springinfra.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.dao.BaseUserDao;
import personal.project.springinfra.dto.UserDto;
import personal.project.springinfra.exception.UserNotExistException;
import personal.project.springinfra.model.domain.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserBL extends BaseBL {

    @Autowired
    private BaseUserDao userDao;

    public User saveOrUpdate(UserDto request) {
        User user = new User();
        if (request.getId() > 0) { //Update
            user = this.userDao.findById(request.getId()).orElseThrow(() -> new UserNotExistException("User doesn't exist"));
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        return this.userDao.save(user);
    }

    public User find(long id) {
        Optional<User> optional = this.userDao.findById(id);
        return optional.orElseThrow(() -> new UserNotExistException("User doesn't exist"));
    }

    public void delete(long id) {
        User user = this.userDao.findById(id).orElseThrow(() -> new UserNotExistException("User doesn't exist"));
        this.userDao.delete(user);
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }
}
