package personal.project.springinfra.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.dto.crud.BaseCrudRequest;

public interface CrudRestApi<D extends BaseCrudRequest> {

    @PostMapping("/save")
    ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request);
}
