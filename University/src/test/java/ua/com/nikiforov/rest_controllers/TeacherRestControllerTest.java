package ua.com.nikiforov.rest_controllers;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

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
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherRestControllerTest extends SetupTestHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherRestControllerTest.class);

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Bob";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Marley";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";


    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private GroupDTO group_1;
    private GroupDTO group_2;
    private GroupDTO group_3;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        teacher_1 = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        teacher_2 = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        teacher_3 = insertTeacher(FIRST_NAME_3, LAST_NAME_3);

        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);
    }

    @Test
    void whenGetAllTeachers_Status200_ReturnTeachersList() throws Exception {
        this.mockMvc.perform(get("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(teacher_2.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(teacher_2.getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(teacher_1.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(teacher_1.getLastName())))
                .andExpect(jsonPath("$[2].firstName", is(teacher_3.getFirstName())))
                .andExpect(jsonPath("$[2].lastName", is(teacher_3.getLastName())));
    }

    @Test
    void whenGetTeacherByValidId_IfSuccessStatus200_ReturnTeacher() throws Exception {
        this.mockMvc.perform(get("/api/teachers/{teacherId}", teacher_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(teacher_1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teacher_1.getLastName())));
    }

    @Test
    void whenAddTeacher_IfSuccessStatus200() throws Exception {
        TeacherDTO newTeacher = new TeacherDTO(FIRST_NAME_4, LAST_NAME_4);
        this.mockMvc.perform(post("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTeacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_4)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_4)));
    }

    @Test
    void whenAddTeacherWithDuplicateNames_Status400() throws Exception {
        String errorMessage = "Error! Already exists Teacher with firstName = Tom, lastName = Hanks";
        StudentDTO newStudent = new StudentDTO(FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        this.mockMvc.perform(post("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errorMessage)));
    }

    @Test
    void whenUpdateTeacher_IfSuccessStatus200() throws Exception {
        TeacherDTO updatedStudent = new TeacherDTO(teacher_3.getId(), FIRST_NAME_3, LAST_NAME_4);
        this.mockMvc.perform(put("/api/teachers/{teacherId}", teacher_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_3)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_4)));
    }

    @Test
    void whenUpdateTeacherWithDuplicateNames_IfSuccessStatus400() throws Exception {
        TeacherDTO updatedTeacher = new TeacherDTO(teacher_3.getId(), FIRST_NAME_2, LAST_NAME_2);
        this.mockMvc.perform(put("/api/teachers/{teacherId}", teacher_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedTeacher)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is("Error! Already exists Teacher with firstName = Bill, lastName = Clinton")));
    }

    @Test
    void whenDeleteTeacherByValidId_Status200_TeacherDelete() throws Exception {
        this.mockMvc.perform(delete("/api/teachers/{teacherId}", teacher_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteTeacherWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/teachers/{teacherId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors", is("Failed to delete Teacher by id 100500")));

    }

    @Test
    void whenAssignTeacherToSubject_Status200_TeacherAssigned() throws Exception {
        List<SubjectDTO> subjects = new ArrayList<>();
        subjects.add(subject_1);
        this.mockMvc.perform(post("/api/teachers/{teacherId}/{subjectId}", teacher_1.getId(), subject_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_1)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_1)))
                .andExpect(jsonPath("$.subjects", is(subjects)));
    }

    @Test
    void whenUnassignTeacherToSubject_Status200_TeacherAssigned() throws Exception {
        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_1.getId());
        List<SubjectDTO> subjects = new ArrayList<>();
        this.mockMvc.perform(post("/api/teachers/{teacherId}/subject/{subjectId}", teacher_1.getId(), subject_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_1)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_1)))
                .andExpect(jsonPath("$.subjects", is(subjects)));
    }

    private String subjectToStringTransformer(List<SubjectDTO> subjects) {
        StringBuilder subjectString = new StringBuilder();
        for (SubjectDTO subjectDTO : subjects) {
            subjectString.append("[{\"id\":" + subjectDTO.getId() +
                    ",\"name\":" + subjectDTO.getName() +
                    ",\"teachers\":[]}]");
        }
        return subjectString.toString();
    }

}