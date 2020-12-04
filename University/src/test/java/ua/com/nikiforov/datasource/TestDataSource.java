package ua.com.nikiforov.datasource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;

import ua.com.nikiforov.config.DatabaseConfig;

@Configuration
@ComponentScan("ua.com.nikiforov")
@Qualifier("test")
public class TestDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataSource.class);
    private static final String DATASOURCE = "datasource.ds";
    private static final String ENVIRONMENT = "java:/comp/env";

    @Bean
    @Qualifier("test")
    DataSource dataSource() {
        DataSource dataSource = null;
        try {
            Context initContex = new InitialContext();
            Context envContext = (Context) initContex.lookup(ENVIRONMENT);
            dataSource = (DataSource) envContext.lookup(DATASOURCE);
        } catch (NamingException e) {
            LOGGER.error("NamingException for {}",DATASOURCE, e);
        }
        return dataSource;
    }

}
