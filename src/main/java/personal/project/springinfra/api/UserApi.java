package personal.project.springinfra.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.assets.ValidationGroups.*;
import personal.project.springinfra.dto.AddUserDto;
import personal.project.springinfra.logic.UserBL;
import personal.project.springinfra.model.domain.User;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserApi extends BaseApi {

    @Autowired
    private UserBL userBL;

    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate> add(@Validated @RequestBody AddUserDto request) {
        User user = this.userBL.add(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseTemplate> update(@Validated(UpdateGroup.class) @RequestBody AddUserDto request) {
        User user = this.userBL.update(request);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseTemplate> find(@Min(1) @PathVariable long id) {
        User user = this.userBL.find(id);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, user));
    }

    @GetMapping("list")
    public List<String> list() {
        return new ArrayList<>();
    }
}
