package springinfra.controller.rest;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springinfra.assets.ErrorCode;
import springinfra.assets.ResponseTemplate;
import springinfra.assets.ValidationGroups;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudRequest;
import springinfra.model.dto.crud.response.CrudApiResponseGenerator;
import springinfra.model.dto.crud.response.DefaultCrudApiResponseGenerator;
import springinfra.service.CrudService;

public interface DefaultCrudRestController<D extends BaseCrudRequest, I extends Number> extends CrudRestController<D, I> {

    CrudApiResponseGenerator crudApiResponseGenerator = new DefaultCrudApiResponseGenerator();

    @PostMapping
    default ResponseEntity<ResponseTemplate> save(@RequestBody @Validated(ValidationGroups.InsertValidationGroup.class) D request) {
        BaseDomain domain = getService().saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getCrudApiResponseGenerator().onSave(domain)));
    }

    @PutMapping
    default ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(ValidationGroups.UpdateValidationGroup.class) D request) {
        BaseDomain domain = getService().saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getCrudApiResponseGenerator().onUpdate(domain)));
    }

    @DeleteMapping("/{id}")
    default ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id) {
        BaseDomain domain = getService().delete(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getCrudApiResponseGenerator().onDelete(domain)));
    }

    @GetMapping("/{id}")
    default ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") I id) {
        BaseDomain domain = getService().find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getCrudApiResponseGenerator().onFind(domain)));
    }

    @GetMapping
    default ResponseEntity<ResponseTemplate> list(Pageable pageable) {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, getCrudApiResponseGenerator().onList(getService().list(pageable))));
    }

    CrudService getService();

    default CrudApiResponseGenerator getCrudApiResponseGenerator() {
        return crudApiResponseGenerator;
    }
}
