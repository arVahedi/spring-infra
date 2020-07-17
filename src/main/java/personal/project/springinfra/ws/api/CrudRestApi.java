package personal.project.springinfra.ws.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.Min;

public interface CrudRestApi<D extends BaseCrudRequest> {

    @PostMapping("/save")
    ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request);

    @PutMapping
    ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) D request);

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id);

    @GetMapping("/{id}")
    ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id);

    @GetMapping("/list")
    ResponseEntity<ResponseTemplate> list();
}
