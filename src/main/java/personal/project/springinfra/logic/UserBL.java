package personal.project.springinfra.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import personal.project.springinfra.dto.AddUserDto;
import personal.project.springinfra.model.domain.User;

@Service
public class UserBL extends BaseBL {

    public User add(AddUserDto request) throws JsonProcessingException {
        System.out.println(String.format("add api called: %s", new ObjectMapper().writeValueAsString(request)));
        return null;
    }
}
