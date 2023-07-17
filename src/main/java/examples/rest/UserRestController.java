package examples.rest;

import examples.domain.User;
import examples.dto.crud.request.UserDto;
import examples.dto.crud.response.UserCrudApiResponseGenerator;
import examples.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.model.dto.crud.response.CrudApiResponseGenerator;
import springinfra.controller.rest.BaseRestController;
import springinfra.controller.rest.DefaultCrudRestController;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/user")
@Slf4j
@Tag(name = "User API", description = "User management API")
@RequiredArgsConstructor
public class UserRestController extends BaseRestController implements DefaultCrudRestController<UserDto, Long> {

    private final UserService userService;
    private CrudApiResponseGenerator<User> crudApiResponseGenerator = new UserCrudApiResponseGenerator();

    @Override
    public UserService getService() {
        return this.userService;
    }

    @Override
    public CrudApiResponseGenerator<User> getCrudApiResponseGenerator() {
        return this.crudApiResponseGenerator;
    }
}
