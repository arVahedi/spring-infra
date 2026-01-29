package examples.rest;

import examples.domain.Credential;
import examples.dto.crud.CredentialDto;
import examples.mapper.CredentialMapper;
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
import org.springinfra.assets.ValidationGroups;
import org.springinfra.controller.rest.BaseRestController;
import org.springinfra.model.dto.GenericDto;
import org.springinfra.utility.identity.IdentityUtility;

import java.util.List;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/credentials")
@Slf4j
@Tag(name = "Credential API", description = "Credential management API")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
@FieldLevelSecurity
public class CredentialRestController extends BaseRestController {

    private final CredentialService credentialService;
    private final CredentialMapper credentialMapper;

    @Operation(summary = "Create credential", description = "Creating a new credential")
    @PostMapping
    public ResponseEntity<ResponseTemplate<CredentialDto>> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) CredentialDto request) {
        Credential credential = this.credentialService.save(request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDto(credential)));
    }

    @Operation(summary = "Update credential", description = "Updating an existing credential")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<CredentialDto>> update(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id,
                                                                  @RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) CredentialDto request) {
        Credential credential = this.credentialService.update(id, request);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDto(credential)));
    }

    @Operation(summary = "Patch credential", description = "Partial updating an existing credential")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseTemplate<CredentialDto>> patch(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id,
                                                                 @RequestBody @ValidateAs(value = "examples.dto.crud.CredentialDto", withGroups = ValidationGroups.UpdateValidationGroup.class) GenericDto genericDto) {
        Credential credential = this.credentialService.patch(id, genericDto);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDto(credential)));
    }

    @Operation(summary = "Delete credential", description = "Deleting an existing credential")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        this.credentialService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Find credential", description = "Retrieving an existing credential")
    @GetMapping("/{id}")
    @FieldLevelSecurity
    public ResponseEntity<ResponseTemplate<CredentialDto>> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        Credential credential = this.credentialService.find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDto(credential)));
    }

    @Operation(summary = "Retrieve credentials", description = "Retrieving all existing credentials")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<CredentialDto>>> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDtos(this.credentialService.list(pageable))));
    }

    @Operation(summary = "Retrieve account info credential", description = "Retrieving the current user's credential")
    @PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).ACCOUNT_INFO_AUTHORITY)")
    @GetMapping("/account-info")
    public ResponseEntity<ResponseTemplate<CredentialDto>> accountInfo() {
        String username = IdentityUtility.getUsername().orElseThrow();
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.credentialMapper.toDto(this.credentialService.findByUsername(username))));
    }
}
