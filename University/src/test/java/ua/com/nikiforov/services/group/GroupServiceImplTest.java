package ua.com.nikiforov.services.group;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.nikiforov.config.UniversityConfig;


class GroupServiceImplTest {

    
       

    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            UniversityConfig.class);
    private GroupServiceImpl groupService = context.getBean(GroupServiceImpl.class);

    @AfterAll
    static void close() {
        context.close();
    }

    @Test
    void test() {
        System.out.println(groupService);
//        System.out.println(groupService.addGroup("AA-12"));
//        System.out.println(groupService.addGroup("AB-12"));
        System.out.println(groupService.getGroupById(1).getGroupName());
        assertEquals("AA-12", groupService.getGroupById(1).getGroupName());
    }

}
