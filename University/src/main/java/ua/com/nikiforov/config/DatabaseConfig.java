package ua.com.nikiforov.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;

import ua.com.nikiforov.exceptions.DataSourceNotInitializeException;

@Configuration
@ComponentScan("ua.com.nikiforov")
public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String JNDI_PATH = "java:comp/env/jdbc/university";

    @Bean
    DataSource dataSource() {
        DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup(JNDI_PATH, DataSource.class);
            LOGGER.debug("DataSourse = {}",dataSource);
            if (dataSource == null) {
                String errorMessage = "DataSourse is not initialized. It is null!";
                LOGGER.error(errorMessage);
                throw new DataSourceNotInitializeException(errorMessage);
            }
        } catch (NamingException e) {
            LOGGER.error("NamingException for {}",JNDI_PATH, e);
        }catch (Exception e) {
            LOGGER.error("Something went wrong while initializing DataSource!",e);
           throw e;
        }
        return dataSource;
    }

}
