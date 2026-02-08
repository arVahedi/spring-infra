package examples.controller.rest;

import examples.model.dto.request.UpdateRoleRequest;
import examples.model.dto.request.CreateRoleRequest;
import examples.model.dto.view.RoleView;
import examples.service.RoleService;
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
import org.springinfra.assets.ResponseTemplate;
import org.springinfra.controller.rest.BaseRestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/roles")
@Slf4j
@Tag(name = "Role API", description = "Role management API")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).ROLE_MANAGEMENT_AUTHORITY)")
public class RoleRestController extends BaseRestController {

    private final RoleService roleService;

    @Operation(summary = "Create role", description = "Creating a new role")
    @PostMapping
    public ResponseEntity<ResponseTemplate<RoleView>> save(@RequestBody @Validated CreateRoleRequest request) {
        var roleView = this.roleService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, roleView));
    }

    @Operation(summary = "Update role", description = "Updating an existing role")
    @PutMapping("/{pId}")
    public ResponseEntity<ResponseTemplate<RoleView>> update(@PathVariable UUID pId,
                                                            @RequestBody @Validated UpdateRoleRequest request) {
        var roleView = this.roleService.update(pId, request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, roleView));
    }

    @Operation(summary = "Delete role", description = "Deleting an existing role")
    @DeleteMapping("/{pId}")
    public ResponseEntity<Void> delete(@PathVariable UUID pId) {
        this.roleService.delete(pId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find role", description = "Retrieving an  existing role")
    @GetMapping("/{pId}")
    public ResponseEntity<ResponseTemplate<RoleView>> find(@PathVariable UUID pId) {
        var roleView = this.roleService.find(pId);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, roleView));
    }

    @Operation(summary = "Retrieve roles", description = "Retrieving all  existing roles")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<RoleView>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.roleService.list(pageable)));
    }
}
