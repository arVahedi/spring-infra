package examples.rest;

import examples.domain.User;
import examples.dto.crud.request.UserDto;
import examples.mapper.UserMapper;
import examples.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springinfra.assets.ResponseTemplate;
import springinfra.assets.ValidationGroups;
import springinfra.controller.rest.BaseRestController;

import java.util.List;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/users")
@Slf4j
@Tag(name = "User API", description = "User management API")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
public class UserRestController extends BaseRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create user", description = "Creating a new user")
    @PostMapping
    public ResponseEntity<ResponseTemplate<UserDto>> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) UserDto request) {
        User user = this.userService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.userMapper.toDto(user)));
    }

    @Operation(summary = "Update user", description = "Updating an exising user")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<UserDto>> update(@PathVariable @Min(value = 0, message = "Minimum acceptable value for id is 1") long id,
                                                            @RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) UserDto request) {
        User user = this.userService.update(id, request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.userMapper.toDto(user)));
    }

    @Operation(summary = "Delete user", description = "Deleting an exising user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find user", description = "Retrieving an exising user")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<UserDto>> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        User user = this.userService.find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.userMapper.toDto(user)));
    }

    @Operation(summary = "Retrieving users", description = "Retrieving all exising users")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<UserDto>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.userMapper.toDtos(this.userService.list(pageable))));
    }
}
