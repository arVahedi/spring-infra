package personal.project.springinfra.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.dto.crud.UserDto;
import personal.project.springinfra.service.DefaultCrudService;
import personal.project.springinfra.service.UserServiceDefault;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/user")
@Slf4j
@Tag(name = "User API", description = "User management API")
public class UserApi extends BaseApi implements DefaultCrudRestApi<UserDto> {

    @Autowired
    private UserServiceDefault userService;

    @Override
    public DefaultCrudService getService() {
        return this.userService;
    }
}
