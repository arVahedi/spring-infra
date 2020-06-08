package personal.project.springinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class SpringInfraApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringInfraApplication.class, args);
	}

}
