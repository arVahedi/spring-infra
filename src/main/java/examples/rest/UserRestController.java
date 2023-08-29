package examples.rest;

import examples.domain.User;
import examples.dto.crud.request.UserDto;
import examples.dto.crud.response.UserCrudApiResponseGenerator;
import examples.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.assets.ResponseTemplate;
import springinfra.controller.rest.BaseRestController;
import springinfra.controller.rest.DefaultCrudRestController;
import springinfra.model.dto.crud.response.CrudApiResponseGenerator;

import java.util.List;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/users")
@Slf4j
@Tag(name = "User API", description = "User management API")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority(T(springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
public class UserRestController extends BaseRestController implements DefaultCrudRestController<UserDto, Long> {

    private final UserService userService;
    private CrudApiResponseGenerator<User> crudApiResponseGenerator = new UserCrudApiResponseGenerator();

    @GetMapping("/au")
    public ResponseEntity<ResponseTemplate<List<UserDto>>> getUsersWithAuth() {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, (List<UserDto>) getCrudApiResponseGenerator().onList(this.userService.list())));
    }

    @Override
    public UserService getService() {
        return this.userService;
    }

    @Override
    public CrudApiResponseGenerator<User> getCrudApiResponseGenerator() {
        return this.crudApiResponseGenerator;
    }
}
