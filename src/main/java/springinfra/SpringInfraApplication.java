package springinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"springinfra", "examples"})
@EnableScheduling
@ServletComponentScan
public class SpringInfraApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInfraApplication.class, args);
    }

    /*@EventListener(WebServerInitializedEvent.class)
    public void eventOne() {
        System.out.println("WebServerInitializedEvent");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void eventTwo() {
        System.out.println("ApplicationReadyEvent");
    }

    @EventListener(EmbeddedServletContainerInitializedEvent.class)
    public void eventThree() {
        System.out.println("EmbeddedServletContainerInitializedEvent");
    }*/
}
