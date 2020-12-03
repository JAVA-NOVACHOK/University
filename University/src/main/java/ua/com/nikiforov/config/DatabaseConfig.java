package ua.com.nikiforov.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;

@Configuration
@ComponentScan("ua.com.nikiforov")
@PropertySource("classpath:university.properties")
public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    @Value("url")
    private String url;
    
    @Value("user")
    private String user;
    
    @Value("driver")
    private String driver;
    
    @Value("password")
    private String password;
    
//    @Bean
//    DataSource dataSource() {
//        DataSource dataSource = null;
//        JndiTemplate jndi = new JndiTemplate();
//        try {
//            dataSource = jndi.lookup("java:comp/env/jdbc/university", DataSource.class);
//        } catch (NamingException e) {
//            LOGGER.error("NamingException for java:comp/env/jdbc/university", e);
//        }
//        return dataSource;
//    }

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

