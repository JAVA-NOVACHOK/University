package ua.com.nikiforov;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.nikiforov.config.UniversityConfig;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(UniversityConfig.class);
        ((AnnotationConfigApplicationContext) context).close();

    }

}
