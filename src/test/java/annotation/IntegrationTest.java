package annotation;

import configuration.CleanupDBTestExecutionListener;
import configuration.MockIdentityProviderConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springinfra.SpringInfraApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = SpringInfraApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({MockIdentityProviderConfig.class})
@ExtendWith(SpringExtension.class)
@TestExecutionListeners(
        listeners = CleanupDBTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface IntegrationTest {
}
