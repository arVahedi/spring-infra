package examples.dto.crud.response;

import personal.project.springinfra.model.dto.GenericDto;
import examples.domain.User;
import personal.project.springinfra.model.dto.crud.response.DefaultCrudApiResponseGenerator;

public class UserCrudApiResponseGenerator extends DefaultCrudApiResponseGenerator<User> {

    @Override
    public Object onFind(User entity) {
        GenericDto dto = new GenericDto();
        dto.setProperty("id", entity.getId());
        dto.setProperty("fullName", entity.getFirstName() + " " + entity.getLastName());
        dto.setProperty("email", entity.getEmail());
        dto.setProperty("status", entity.getStatus());
        dto.setProperty("phone", entity.getPhone());
        dto.setProperty("insertDate", entity.getInsertDate().toEpochMilli());
        return dto;
    }
}
