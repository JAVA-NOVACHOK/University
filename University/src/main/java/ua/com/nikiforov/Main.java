package ua.com.nikiforov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;
import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }


    @Bean
    public Docket customizeSwaggerOutput(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("ua.com.nikiforov.rest_controllers"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo(){
        return  new ApiInfo(
                "University",
                "Students or teachers can get their timetable for a day or for a month.",
                "1.0",
                "Free to use",
                new Contact("Artem Nikiforov", "linkedin.com/in/artem-nikiforov-4387981a0","nikiforov.artem.125@gmail.com"),
                "API License",
                "linkedin.com/in/artem-nikiforov-4387981a0",
                 Collections.emptyList()
        );

    }

}
