package ua.com.nikiforov.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ua.com.nikiforov.exceptions.DataSourceNotInitializeException;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan("ua.com.nikiforov")
//@PropertySource("classpath:hibernate.properties")
public class HibernateConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConfig.class);
    private static final String JNDI_PATH = "java:comp/env/jdbc/university";


    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ua.com.nikiforov");
        return sessionFactory;
    }

    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup(JNDI_PATH, BasicDataSource.class);
            LOGGER.debug("DataSourse = {}", dataSource);
        } catch (NamingException e) {
            String errorMessage = String.format("DataSourse is not initialized. JNDI Path is = %s",JNDI_PATH);
            LOGGER.error(errorMessage);
            throw new DataSourceNotInitializeException(errorMessage,e);
        }
        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
