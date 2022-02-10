package springinfra.ws.api;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import springinfra.assets.ResponseTemplate;
import springinfra.assets.ValidationGroups;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.Min;

public interface CrudRestApi<D extends BaseCrudRequest, I extends Number> {

    ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request);

    ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) D request);

    ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id);

    ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id);

    ResponseEntity<ResponseTemplate> list(Pageable pageable);
}
