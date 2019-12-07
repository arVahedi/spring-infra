package personal.project.springinfra.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.dto.AddUserDto;
import personal.project.springinfra.logic.UserBL;
import personal.project.springinfra.model.domain.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserApi extends BaseApi {

    @Autowired
    private UserBL userBL;

    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate> add(@Valid @RequestBody AddUserDto request) throws JsonProcessingException {
        User user = this.userBL.add(request);
        return ResponseEntity.ok(new ResponseTemplate(ErrorCode.NO_ERROR));
    }

    @GetMapping("list")
    public List<String> list() {
        return new ArrayList<>();
    }
}
