package examples.rest;

import examples.domain.Role;
import examples.dto.crud.request.RoleDto;
import examples.mapper.RoleMapper;
import examples.service.RoleService;
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
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/roles")
@Slf4j
@Tag(name = "Role API", description = "Role management API")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
public class RoleRestController extends BaseRestController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @Operation(summary = "Create role", description = "Creating a new role")
    @PostMapping
    public ResponseEntity<ResponseTemplate<RoleDto>> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) RoleDto request) {
        Role role = this.roleService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.roleMapper.toDto(role)));
    }

    @Operation(summary = "Update role", description = "Updating an existing role")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<RoleDto>> update(@PathVariable @Min(value = 0, message = "Minimum acceptable value for id is 1") int id,
                                                            @RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) RoleDto request) {
        Role role = this.roleService.update(id, request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.roleMapper.toDto(role)));
    }

    @Operation(summary = "Delete role", description = "Deleting an existing role")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") int id) {
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find role", description = "Retrieving an exising role")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<RoleDto>> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") int id) {
        Role role = this.roleService.find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.roleMapper.toDto(role)));
    }

    @Operation(summary = "Retrieve roles", description = "Retrieving all exising roles")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<RoleDto>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.roleMapper.toDtos(this.roleService.list(pageable))));
    }
}
