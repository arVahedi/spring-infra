package examples.dto.crud.response;

import examples.domain.Credential;
import personal.project.springinfra.assets.Constant;
import personal.project.springinfra.model.dto.crud.response.DefaultCrudApiResponseGenerator;

public class CredentialCrudApiResponseGenerator extends DefaultCrudApiResponseGenerator<Credential> {

    @Override
    public Object onGeneral(Credential entity) {
        entity.setPassword(Constant.PASSWORD_MASK);
        return entity;
    }
}
