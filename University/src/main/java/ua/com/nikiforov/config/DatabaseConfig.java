package ua.com.nikiforov.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("ua.com.nikiforov")
@PropertySource("classpath:university.properties")
public class DatabaseConfig {

    @Value("url")
    private String url;
    
    @Value("user")
    private String user;
    
    @Value("driver")
    private String driver;
    
    @Value("password")
    private String password;
    
    @Bean
    DataSource dataSource(Environment environment) {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(url));
        driverManagerDataSource.setUsername(environment.getProperty(user));
        driverManagerDataSource.setPassword(environment.getProperty(password));
        driverManagerDataSource.setDriverClassName(environment.getProperty(driver));
        return driverManagerDataSource;
    }

}

