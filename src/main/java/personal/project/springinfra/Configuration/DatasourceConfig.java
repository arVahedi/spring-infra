package personal.project.springinfra.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import personal.project.springinfra.database.repository.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class, basePackages = "personal.project.springinfra")
@EnableTransactionManagement
public class DatasourceConfig extends BaseConfig {
}
