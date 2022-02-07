package springinfra.ws.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springinfra.assets.ResponseTemplate;
import springinfra.assets.ValidationGroups;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.Min;

public interface CrudRestApi<D extends BaseCrudRequest, I extends Number> {

    @PostMapping("/save")
    ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request);

    @PutMapping
    ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) D request);

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id);

    @GetMapping("/{id}")
    ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id);

    @GetMapping("/list")
    ResponseEntity<ResponseTemplate> list();
}
