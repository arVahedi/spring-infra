package examples.api;

import examples.domain.Credential;
import examples.dto.crud.request.CredentialDto;
import examples.dto.crud.response.CredentialCrudApiResponseGenerator;
import examples.service.CredentialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.model.dto.crud.response.CrudApiResponseGenerator;
import personal.project.springinfra.service.CrudService;
import personal.project.springinfra.ws.api.BaseApi;
import personal.project.springinfra.ws.api.DefaultCrudRestApi;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
@Slf4j
@Tag(name = "Credential API", description = "Credential management API")
@RequiredArgsConstructor
public class CredentialApi extends BaseApi implements DefaultCrudRestApi<CredentialDto> {

    private final CredentialService credentialService;
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
