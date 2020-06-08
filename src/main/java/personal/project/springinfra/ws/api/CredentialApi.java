package personal.project.springinfra.ws.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.dto.crud.CredentialDto;
import personal.project.springinfra.dto.generator.CredentialCrudApiResponseGenerator;
import personal.project.springinfra.dto.generator.CrudApiResponseGenerator;
import personal.project.springinfra.model.domain.Credential;
import personal.project.springinfra.service.CredentialService;
import personal.project.springinfra.service.CrudService;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
@Slf4j
@Tag(name = "Credential API", description = "Credential management API")
public class CredentialApi extends BaseApi implements DefaultCrudRestApi<CredentialDto> {

    @Autowired
    private CredentialService credentialService;
    private CrudApiResponseGenerator<Credential> crudApiResponseGenerator = new CredentialCrudApiResponseGenerator();

    @Override
    public CrudService getService() {
        return this.credentialService;
    }

    @Override
    public CrudApiResponseGenerator getCrudApiResponseGenerator() {
        return this.crudApiResponseGenerator;
    }
}
