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
import personal.project.springinfra.dto.crud.CredentialDto;
import personal.project.springinfra.model.domain.Credential;
import personal.project.springinfra.service.CredentialService;
import personal.project.springinfra.service.DefaultCrudService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
@Slf4j
@Tag(name="Credential API", description = "Credential management API")
public class CredentialApi extends BaseApi implements DefaultCrudRestApi<CredentialDto>{

    @Autowired
    private CredentialService credentialService;

    @Override
    public DefaultCrudService getService() {
        return this.credentialService;
    }
}
