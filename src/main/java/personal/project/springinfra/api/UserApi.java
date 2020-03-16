package personal.project.springinfra.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.dto.crud.UserDto;
import personal.project.springinfra.dto.generator.CrudApiResponseGenerator;
import personal.project.springinfra.dto.generator.UserCrudApiResponseGenerator;
import personal.project.springinfra.model.domain.User;
import personal.project.springinfra.service.CrudService;
import personal.project.springinfra.service.UserService;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/user")
@Slf4j
@Tag(name = "User API", description = "User management API")
public class UserApi extends BaseApi implements DefaultCrudRestApi<UserDto> {

    @Autowired
    private UserService userService;
    private CrudApiResponseGenerator<User> crudApiResponseGenerator = new UserCrudApiResponseGenerator();

    @Override
    public CrudService getService() {
        return this.userService;
    }

    @Override
    public CrudApiResponseGenerator<User> getCrudApiResponseGenerator() {
        return this.crudApiResponseGenerator;
    }
}
