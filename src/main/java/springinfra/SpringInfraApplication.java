package springinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"springinfra", "examples"})
@EnableScheduling
@ServletComponentScan
@EnableConfigurationProperties
public class SpringInfraApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInfraApplication.class, args);
    }
}
