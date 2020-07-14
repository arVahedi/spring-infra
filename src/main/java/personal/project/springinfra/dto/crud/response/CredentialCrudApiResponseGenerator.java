package personal.project.springinfra.dto.crud.response;

import personal.project.springinfra.assets.Constants;
import personal.project.springinfra.model.domain.Credential;

public class CredentialCrudApiResponseGenerator extends DefaultCrudApiResponseGenerator<Credential> {

    @Override
    public Object onGeneral(Credential entity) {
        entity.setPassword(Constants.PASSWORD_PLACEHOLDER);
        entity.setSalt(null);
        return entity;
    }
}
