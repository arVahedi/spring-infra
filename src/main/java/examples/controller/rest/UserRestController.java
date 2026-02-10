package examples.controller.rest;

import examples.model.dto.request.CreateUserRequest;
import examples.model.dto.request.UpdateUserRequest;
import examples.model.dto.view.UserView;
import examples.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springinfra.annotation.validation.ValidateAs;
import org.springinfra.assets.ResponseTemplate;
import org.springinfra.controller.rest.BaseRestController;
import org.springinfra.model.dto.request.PropertyBagRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/users")
@Slf4j
@Tag(name = "User API", description = "User management API")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
public class UserRestController extends BaseRestController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "Creating a new user")
    @PostMapping
    public ResponseEntity<ResponseTemplate<UserView>> save(@RequestBody @Validated CreateUserRequest request) {
        var userView = this.userService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, userView));
    }

    @Operation(summary = "Patch user", description = "Partial updating an existing user")
    @PatchMapping("/{pId}")
    public ResponseEntity<ResponseTemplate<UserView>> patch(@PathVariable UUID pId,
                                                            @RequestBody @ValidateAs(UpdateUserRequest.class) PropertyBagRequest propertyBagRequest) {
        var userView = this.userService.patch(pId, propertyBagRequest);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, userView));
    }

    @Operation(summary = "Delete user", description = "Deleting an existing user")
    @DeleteMapping("/{pId}")
    public ResponseEntity<Void> delete(@PathVariable UUID pId) {
        this.userService.delete(pId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find user", description = "Retrieving an existing user")
    @GetMapping("/{pId}")
    public ResponseEntity<ResponseTemplate<UserView>> find(@PathVariable UUID pId) {
        var userView = this.userService.find(pId);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, userView));
    }

    @Operation(summary = "Retrieving users", description = "Retrieving all existing users")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<UserView>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.userService.list(pageable)));
    }
}
