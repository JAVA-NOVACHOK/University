package ua.com.nikiforov.rest_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentsRestControllerTest extends SetupTestHelper {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Jay";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Prichet";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AB-13";
    private static final String TEST_GROUP_NAME_3 = "AB-14";

    private GroupDTO groupFrom;
    private GroupDTO group_1;
    private GroupDTO group_2;
    private StudentDTO student_1;
    private StudentDTO student_2;
    private StudentDTO student_3;

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
        this.mockMvc.perform(get("/api/students/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(student_2.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(student_2.getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(student_1.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(student_1.getLastName())))
                .andExpect(jsonPath("$[2].firstName", is(student_3.getFirstName())))
                .andExpect(jsonPath("$[2].lastName", is(student_3.getLastName())));
    }

    @Test
    void whenGetStudentByValidId_IfSuccessStatus200_ReturnStudent() throws Exception {
        this.mockMvc.perform(get("/api/students/{studentId}", student_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(student_1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student_1.getLastName())));
    }

    @Test
    void whenAddStudent_IfSuccessStatus200() throws Exception {
        StudentDTO newStudent = new StudentDTO(FIRST_NAME_4, LAST_NAME_4, group_1.getGroupId());
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_4)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_4)));
    }

    @Test
    void whenAddStudentWithDuplicateNames_Status400() throws Exception {
        String errorMessage = "Error! Already exists Student with firstName = Tom, lastName = Hanks, groupId = 1";
        StudentDTO newStudent = new StudentDTO(FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", is(errorMessage)));
    }

    @Test
    void whenUpdateStudent_IfSuccessStatus200() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(student_3.getId(), FIRST_NAME_3, LAST_NAME_4, group_1.getGroupId());
        this.mockMvc.perform(put("/api/students/{studentId}", student_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_3)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_4)));
    }

    @Test
    void whenUpdateStudentWithDuplicateNames_IfSuccessStatus400() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(student_3.getId(), FIRST_NAME_2, LAST_NAME_2, group_1.getGroupId());
        this.mockMvc.perform(put("/api/students/{studentId}", student_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", is("Error! Already exists Student with firstName = Bill, lastName = Clinton, groupId = 1")));
    }

    @Test
    void whenDeleteStudentByValidId_Status200_StudentDelete() throws Exception {
        this.mockMvc.perform(delete("/api/students/{studentId}", student_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteStudentWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/students/{studentId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

    }

}