package personal.project.springinfra.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public User add(AddUserDto request) throws JsonProcessingException {
        System.out.println(String.format("add api called: %s", new ObjectMapper().writeValueAsString(request)));
        User user = new User();
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
