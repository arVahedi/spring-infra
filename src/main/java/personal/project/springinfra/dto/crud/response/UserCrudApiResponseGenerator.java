package personal.project.springinfra.dto.crud.response;

import personal.project.springinfra.dto.GenericDto;
import personal.project.springinfra.model.domain.User;

public class UserCrudApiResponseGenerator extends DefaultCrudApiResponseGenerator<User> {

    @Override
    public Object onFind(User entity) {
        GenericDto dto = new GenericDto();
        dto.setProperty("id", entity.getId());
        dto.setProperty("fullName", entity.getFirstName() + " " + entity.getLastName());
        dto.setProperty("email", entity.getEmail());
        dto.setProperty("status", entity.getStatus());
        dto.setProperty("phone", entity.getPhone());
        dto.setProperty("insertDate", entity.getInsertDate().getTime());
        return dto;
    }
}
