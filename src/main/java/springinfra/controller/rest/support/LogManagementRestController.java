package springinfra.controller.rest.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.assets.ResponseTemplate;
import springinfra.controller.rest.BaseRestController;
import springinfra.service.LogManagementService;

@Slf4j
@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/support/log")
@Tag(name = "Log Management API", description = "Management level of loggers in runtime")
@RequiredArgsConstructor
public class LogManagementRestController extends BaseRestController {

    private final LogManagementService logManagementService;

    @Operation(summary = "Get logger level", description = "Get level of a logger")
    @GetMapping("/level/get/{name}")
    public ResponseEntity<ResponseTemplate<String>> getLoggerLevel(
            @PathVariable @NotBlank(message = "logger name is required") String name) {
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, this.logManagementService.getLoggerLevel(name).name()));
    }

    @Operation(summary = "Set logger level", description = "Set level of a logger")
    @GetMapping(value = "/level/set/{name}/{level}")
    public ResponseEntity<Void> setLoggerLevel(
            @PathVariable @NotBlank(message = "logger name is required") String name,
            @PathVariable @NotBlank(message = "logger level is required") @Pattern(regexp = "OFF|ERROR|WARN|INFO|DEBUG|TRACE", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Illegal value for level of logger") String level) {
        this.logManagementService.setLoggerLevel(name, level);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
