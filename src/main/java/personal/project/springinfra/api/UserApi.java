package personal.project.springinfra.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "User rest api", description = "All operation about user supported by this api")
public class UserApi extends BaseApi {

    @Autowired
    private UserBL userBL;

    @PostMapping("/add")
    @ApiOperation(value = "Add new user", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> add(@Validated(InsertValidationGroup.class) @RequestBody UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @PostMapping("/update")
    @ApiOperation(value = "Update an existing user", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> update(@Validated(UpdateValidationGroup.class) @RequestBody UserDto request) {
        User user = this.userBL.saveOrUpdate(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @GetMapping("/find/{id}")
    @ApiOperation(value = "Find an existing user", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> find(@Min(1) @PathVariable long id) {
        User user = this.userBL.find(id);
        GenericDto genericDto = new GenericDto();
        genericDto.setProperty("FullName", user.getFirstName() + " " + user.getLastName());
        genericDto.setProperty("email", user.getEmail());
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, genericDto));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "Delete an existing user", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> delete(@Min(1) @PathVariable long id) {
        this.userBL.delete(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, id));
    }

    @GetMapping("/list")
    @ApiOperation(value = "Get all users as list", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> list() {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, this.userBL.findAll()));
    }
}
