package examples.api;

import examples.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import examples.dto.crud.request.UserDto;
import personal.project.springinfra.model.dto.crud.response.CrudApiResponseGenerator;
import examples.dto.crud.response.UserCrudApiResponseGenerator;
import examples.domain.User;
import personal.project.springinfra.service.CrudService;
import personal.project.springinfra.ws.api.BaseApi;
import personal.project.springinfra.ws.api.DefaultCrudRestApi;

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
