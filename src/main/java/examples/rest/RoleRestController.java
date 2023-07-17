package examples.rest;

import examples.dto.crud.request.RoleDto;
import examples.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.controller.rest.BaseRestController;
import springinfra.controller.rest.DefaultCrudRestController;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/role")
@Slf4j
@Tag(name = "Role API", description = "Role management API")
@RequiredArgsConstructor
public class RoleRestController extends BaseRestController implements DefaultCrudRestController<RoleDto, Integer> {

    private final RoleService roleService;

    @Override
    public RoleService getService() {
        return this.roleService;
    }
}
