package personal.project.springinfra.api.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.api.BaseApi;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.logic.LogManagementBL;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/support/log")
@Api(value = "Log Management rest api", description = "Management loggers in runtime")
public class LogManagementApi extends BaseApi {

    @Autowired
    private LogManagementBL logManagementBL;

    @GetMapping("/level/get/{name}")
    @ApiOperation(value = "Get level of specific logger", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> getLoggerLevel(@NotBlank(message = "logger name is required") @PathVariable String name) {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, this.logManagementBL.getLoggerLevel(name).name()));
    }

    @GetMapping("/level/set/{name}/{level}")
    @ApiOperation(value = "Change level of specific logger", response = ResponseEntity.class)
    public ResponseEntity<ResponseTemplate> setLoggerLevel(@NotBlank(message = "logger name is required") @PathVariable String name,
                                                           @NotBlank(message = "logger level is required") @PathVariable String level) {
        this.logManagementBL.setLoggerLevel(name, level);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR));
    }
}
