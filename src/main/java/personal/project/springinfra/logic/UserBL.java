package personal.project.springinfra.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.dao.BaseUserDao;
import personal.project.springinfra.dto.AddUserDto;
import personal.project.springinfra.exception.UserNotExistException;
import personal.project.springinfra.model.domain.User;

import java.util.Optional;

@Service
public class UserBL extends BaseBL {

    @Autowired
    private BaseUserDao userDao;

    public User add(AddUserDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        return this.userDao.save(user);
    }

    public User update(AddUserDto request) {
        User user = this.userDao.findById(request.getId()).orElseThrow(UserNotExistException::new);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        return this.userDao.save(user);
    }

    public User find(long id) {
        Optional<User> optional = this.userDao.findById(id);
        return optional.orElseThrow(UserNotExistException::new);
    }
}
