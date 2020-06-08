package personal.project.springinfra.ws.api.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.springinfra.ws.api.BaseApi;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.service.LogManagementService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/support/log")
@Tag(name="Log Management API", description = "Management level of loggers in runtime")
public class LogManagementApi extends BaseApi {

    @Autowired
    private LogManagementService logManagementService;

    @GetMapping("/level/get/{name}")
    public ResponseEntity<ResponseTemplate> getLoggerLevel(
            @PathVariable @NotBlank(message = "logger name is required") String name) {
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR, this.logManagementService.getLoggerLevel(name).name()));
    }

    @GetMapping(value = "/level/set/{name}/{level}")
    public ResponseEntity<ResponseTemplate> setLoggerLevel(
            @PathVariable @NotBlank(message = "logger name is required") String name,
            @PathVariable @NotBlank(message = "logger level is required") @Pattern(regexp = "OFF|ERROR|WARN|INFO|DEBUG|TRACE", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Illegal value for level of logger") String level) {
        this.logManagementService.setLoggerLevel(name, level);
        return ResponseEntity.ok(new ResponseTemplate<>(ErrorCode.NO_ERROR));
    }
}
