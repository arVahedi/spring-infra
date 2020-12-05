package examples.dto.crud.response;

import examples.domain.Credential;
import personal.project.springinfra.assets.Constants;
import personal.project.springinfra.model.dto.crud.response.DefaultCrudApiResponseGenerator;

public class CredentialCrudApiResponseGenerator extends DefaultCrudApiResponseGenerator<Credential> {

    @Override
    public Object onGeneral(Credential entity) {
        entity.setPassword(Constants.PASSWORD_PLACEHOLDER);
        return entity;
    }
}
