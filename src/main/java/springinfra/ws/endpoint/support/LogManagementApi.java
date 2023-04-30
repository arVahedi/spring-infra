package springinfra.ws.endpoint.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.assets.ErrorCode;
import springinfra.assets.ResponseTemplate;
import springinfra.service.LogManagementService;
import springinfra.ws.endpoint.BaseApi;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Slf4j
@RestController
@RequestMapping(BaseApi.API_PATH_PREFIX_V1 + "/support/log")
@Tag(name = "Log Management API", description = "Management level of loggers in runtime")
@RequiredArgsConstructor
public class LogManagementApi extends BaseApi {

    private final LogManagementService logManagementService;

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
