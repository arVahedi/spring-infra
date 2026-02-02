package examples.controller.rest;

import examples.model.dto.CreateCredentialRequest;
import examples.model.view.CredentialView;
import examples.service.CredentialService;
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
import org.springinfra.annotation.FieldLevelSecurity;
import org.springinfra.annotation.validation.ValidateAs;
import org.springinfra.assets.ResponseTemplate;
import org.springinfra.controller.rest.BaseRestController;
import org.springinfra.model.dto.PropertyBagDto;
import org.springinfra.utility.identity.IdentityUtil;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/credentials")
@Tag(name = "Credential API", description = "Credential management API")
@PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
@RequiredArgsConstructor
@FieldLevelSecurity
public class CredentialRestController extends BaseRestController {

    private final CredentialService credentialService;

    @Operation(summary = "Create credential", description = "Creating a new credential")
    @PostMapping
    public ResponseEntity<ResponseTemplate<CredentialView>> save(@RequestBody @Validated CreateCredentialRequest request) {
        var credentialView = this.credentialService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, credentialView));
    }

    @Operation(summary = "Patch credential", description = "Partial updating an existing credential")
    @PatchMapping("/{pId}")
    public ResponseEntity<ResponseTemplate<CredentialView>> patch(@PathVariable UUID pId,
                                                                  @RequestBody @ValidateAs(value = "examples.model.dto.UpdateCredentialRequest") PropertyBagDto propertyBagDto) {
        var credentialView = this.credentialService.patch(pId, propertyBagDto);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, credentialView));
    }

    @Operation(summary = "Delete credential", description = "Deleting an existing credential")
    @DeleteMapping("/{pId}")
    public ResponseEntity<Void> delete(@PathVariable UUID pId) {
        this.credentialService.delete(pId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find credential", description = "Retrieving an existing credential")
    @GetMapping("/{pId}")
    @FieldLevelSecurity
    public ResponseEntity<ResponseTemplate<CredentialView>> find(@PathVariable UUID pId) {
        var credentialView = this.credentialService.findById(pId);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, credentialView));
    }

    @Operation(summary = "Retrieve credentials", description = "Retrieving all existing credentials")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<CredentialView>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialService.findAll(pageable)));
    }

    @Operation(summary = "Retrieve account info credential", description = "Retrieving the current user's credential")
    @PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).ACCOUNT_INFO_AUTHORITY)")
    @GetMapping("/account-info")
    public ResponseEntity<ResponseTemplate<CredentialView>> accountInfo() {
        String username = IdentityUtil.getUsername().orElseThrow();
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialService.findByUsername(username)));
    }
}
