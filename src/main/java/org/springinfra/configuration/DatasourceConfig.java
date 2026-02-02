package org.springinfra.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springinfra.database.repository.BaseRepository;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"org/springinfra", "examples"})
@EntityScan(basePackages = {"org/springinfra", "examples"})
@EnableTransactionManagement
public class DatasourceConfig implements BaseConfig {
}
