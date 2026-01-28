package springinfra.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springinfra.database.repository.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class, basePackages = {"springinfra", "examples"})
@EntityScan(basePackages = {"springinfra", "examples"})
@EnableTransactionManagement
public class DatasourceConfig implements BaseConfig {
}
