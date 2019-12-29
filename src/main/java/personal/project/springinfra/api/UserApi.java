package personal.project.springinfra.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.assets.ValidationGroups.InsertValidationGroup;
import personal.project.springinfra.assets.ValidationGroups.UpdateValidationGroup;
import personal.project.springinfra.dto.GenericDto;
import personal.project.springinfra.dto.UserDto;
import personal.project.springinfra.logic.UserBL;
import personal.project.springinfra.model.domain.User;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/user")
@Slf4j
@Tag(name="User API", description = "User management API")
public class UserApi extends BaseApi {

    @Autowired
    private UserBL userBL;

    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate> add(@RequestBody @Validated(InsertValidationGroup.class) UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseTemplate> update(@RequestBody @Validated(UpdateValidationGroup.class) UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseTemplate> find(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        User user = this.userBL.find(id);
        GenericDto genericDto = new GenericDto();
        genericDto.setProperty("FullName", user.getFirstName() + " " + user.getLastName());
        genericDto.setProperty("email", user.getEmail());
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, genericDto));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseTemplate> delete(@PathVariable @Min(value = 1, message = "Minimum acceptable value for id is 1") long id) {
        this.userBL.delete(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, id));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseTemplate> list() {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, this.userBL.findAll()));
    }
}
