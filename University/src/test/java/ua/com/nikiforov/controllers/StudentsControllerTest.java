package ua.com.nikiforov.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.nikiforov.config.WebConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
class StudentsControllerTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final String TEST_GROUP_NAME_1 = "AA-12";

    @Autowired
    private TableCreator tableCreator;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void givenStudentURI_whenMockMVC_thenReturnStudentView_WithStudentGroupModelAttributes() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        Student student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroup_1.getId());
        Student student_2 = insertStudent(FIRST_NAME_2, LAST_NAME_2, testGroup_1.getId());
        Student student_3 = insertStudent(FIRST_NAME_3, LAST_NAME_3, testGroup_1.getId());
        this.mockMvc.perform(get("/students/?id={id}", testGroup_1.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("students", hasItems(student_2, student_1, student_3)))
                .andExpect(model().attribute("group", testGroup_1))
                .andExpect(view().name("students"));
    }

    private Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    private Student insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(firstName, lastaName, groupName);
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
    }

}
