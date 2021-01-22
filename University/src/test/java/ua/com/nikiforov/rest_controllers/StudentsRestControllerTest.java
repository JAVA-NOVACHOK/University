package ua.com.nikiforov.rest_controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentsRestControllerTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AB-13";
    private static final String TEST_GROUP_NAME_3 = "AB-14";

    private static final String JSON_ROOT = "$";

    private static final String GROUP_ID_ATTR = "groupId";
    private static final String GROUP_NAME_ATTR = "groupName";
    private static final String GROUP_TO_ID_ATTR = "groupToId";
    private static final String STUDENT_ATTR = "student";
    private static final String STUDENT_ID_ATTR = "studentId";
    private static final String ID = "id";

    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_EDIT_FORM = "students/edit_form";
    private static final String VIEW_TRANSFER_FORM = "students/transfer_form";

    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUP_ATTR = "group";
    private static final String GROUPS_ATTR = "groups";
    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";


    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";

    private GroupDTO groupFrom;
    private GroupDTO group_1;
    private GroupDTO group_2;
    private StudentDTO student_1;
    private StudentDTO student_2;
    private StudentDTO student_3;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        groupFrom = insertGroup(TEST_GROUP_NAME_3);
        student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        student_2 = insertStudent(FIRST_NAME_2, LAST_NAME_2, group_1.getGroupId());
        student_3 = insertStudent(FIRST_NAME_3, LAST_NAME_3, group_1.getGroupId());

    }

    @Test
    void whenGetAllStudents_Status200_ReturnStudentsList() throws Exception {
        this.mockMvc.perform(get("/api/students/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(student_2.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(student_2.getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(student_1.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(student_1.getLastName())))
                .andExpect(jsonPath("$[2].firstName", is(student_3.getFirstName())))
                .andExpect(jsonPath("$[2].lastName", is(student_3.getLastName())));
    }

    private GroupDTO insertGroup(String groupName) {
        return groupService.addGroup(new GroupDTO(groupName));
    }

    private StudentDTO insertStudent(String firstName, String lastName, long groupId) {
        return studentsService.addStudent(new StudentDTO(firstName, lastName, groupId));
    }

}