package personal.project.springinfra.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.dto.GenericDto;
import personal.project.springinfra.model.domain.Credential;
import personal.project.springinfra.service.CredentialServiceDefault;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
@Slf4j
@Tag(name="Credential API", description = "Credential management API")
public class CredentialApi extends BaseApi {

    @Autowired
    private CredentialServiceDefault credentialService;

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        Credential credential = this.credentialService.find(id);
        GenericDto genericDto = new GenericDto();
        genericDto.setProperty("username", credential.getUsername());
        genericDto.setProperty("password", "*******");
        genericDto.setProperty("fullName", credential.getUser().getFirstName() + " " + credential.getUser().getLastName());
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, genericDto));
    }


}
