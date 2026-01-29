package org.springinfra.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springinfra.database.repository.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class, basePackages = {"org/springinfra", "examples"})
@EntityScan(basePackages = {"org/springinfra", "examples"})
@EnableTransactionManagement
public class DatasourceConfig implements BaseConfig {
}
