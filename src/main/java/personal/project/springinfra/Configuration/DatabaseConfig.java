package personal.project.springinfra.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig extends BaseConfig {

    @Value("${datasource.jdbcUrl}")
    private String jdbcUrl;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.hibernate.dialect}")
    private String hibernateDialect;
    @Value("${datasource.hibernate.hbm2ddl}")
    private String hibernateHbm2ddl;
    @Value("${datasource.hibernate.showSql}")
    private String hibernateShowSql;
    @Value("${datasource.hibernate.formatSql}")
    private String hibernateFormatSql;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[]{"personal.project.springinfra.model.domain"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.jdbcUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        return new HikariDataSource(config);
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", this.hibernateHbm2ddl);
        hibernateProperties.setProperty("hibernate.dialect", this.hibernateDialect);
        hibernateProperties.setProperty("hibernate.archive.autodetection", "class");
        hibernateProperties.setProperty("hibernate.show_sql", this.hibernateShowSql);
        hibernateProperties.setProperty("hibernate.format_sql", this.hibernateFormatSql);

        return hibernateProperties;
    }
}
