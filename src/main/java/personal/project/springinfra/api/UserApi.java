package personal.project.springinfra.api;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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
//@Slf4j
@Log4j2
public class UserApi extends BaseApi {

    @Autowired
    private UserBL userBL;

    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate> add(@Validated(InsertValidationGroup.class) @RequestBody UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseTemplate> update(@Validated(UpdateValidationGroup.class) @RequestBody UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseTemplate> find(@Min(1) @PathVariable long id) {

        log.fatal(String.format("/find api called [ID: %d]", id));
        log.error(String.format("/find api called [ID: %d]", id));
        log.warn(String.format("/find api called [ID: %d]", id));
        log.info(String.format("/find api called [ID: %d]", id));
        log.debug(String.format("/find api called [ID: %d]", id));
        log.trace(String.format("/find api called [ID: %d]", id));

        User user = this.userBL.find(id);
        GenericDto genericDto = new GenericDto();
        genericDto.setProperty("FullName", user.getFirstName() + " " + user.getLastName());
        genericDto.setProperty("email", user.getEmail());
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, genericDto));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseTemplate> delete(@Min(1) @PathVariable long id) {
        this.userBL.delete(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, id));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseTemplate> list() {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, this.userBL.findAll()));
    }
}
