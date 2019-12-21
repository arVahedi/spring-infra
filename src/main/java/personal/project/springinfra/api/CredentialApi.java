package personal.project.springinfra.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/credential")
public class CredentialApi extends BaseApi {


}
