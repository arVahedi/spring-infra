package personal.project.springinfra.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.dto.crud.BaseCrudRequest;
import personal.project.springinfra.model.domain.BaseDomain;
import personal.project.springinfra.service.DefaultCrudService;

import javax.validation.constraints.Min;

public interface DefaultCrudRestApi<D extends BaseCrudRequest> extends CrudRestApi<D> {

    @PostMapping
    default ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request) {
        BaseDomain domain = getService().saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, domain));
    }

    @PutMapping
    default ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) D request) {
        BaseDomain domain = getService().saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, domain));
    }

    @DeleteMapping("/{id}")
    default ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        getService().delete(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, id));
    }

    @GetMapping("/{id}")
    default ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        BaseDomain domain = getService().find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, domain));
    }

    @GetMapping("/list")
    default ResponseEntity<ResponseTemplate> list() {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getService().findAll()));
    }

    DefaultCrudService getService();
}
