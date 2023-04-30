package examples.endpoint;

import examples.domain.Credential;
import examples.dto.crud.request.CredentialDto;
import examples.dto.crud.response.CredentialCrudApiResponseGenerator;
import examples.service.CredentialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.model.dto.crud.response.CrudApiResponseGenerator;
import springinfra.ws.endpoint.BaseApi;
import springinfra.ws.endpoint.DefaultCrudRestApi;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
@Slf4j
@Tag(name = "Credential API", description = "Credential management API")
@RequiredArgsConstructor
public class CredentialApi extends BaseApi implements DefaultCrudRestApi<CredentialDto, Long> {

    private final CredentialService credentialService;
    private CrudApiResponseGenerator<Credential> crudApiResponseGenerator = new CredentialCrudApiResponseGenerator();

    @Override
    public CredentialService getService() {
        return this.credentialService;
    }

    @Override
    public CrudApiResponseGenerator<Credential> getCrudApiResponseGenerator() {
        return this.crudApiResponseGenerator;
    }
}
