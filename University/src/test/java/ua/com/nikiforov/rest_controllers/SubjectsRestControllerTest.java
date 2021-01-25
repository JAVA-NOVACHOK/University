package ua.com.nikiforov.rest_controllers;

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
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SubjectsRestControllerTest extends SetupTestHelper {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";
    private static final String SUBJECT_NAME_4 = "Java";
    private static final String SUBJECT_NAME_5 = "WordPress";
    private static final String SUBJECT_INVALID_NAME = "D";



    private static final String NAME = "$.name";

    private static final String JSON_ROOT = "$";

    private static final long INVALID_ID = 100500l;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);
    }

    @Test
    void whenGetAllSubjects_Status200_ReturnSubjectsList() throws Exception {
        this.mockMvc.perform(get("/api/subjects/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(subject_3.getName())))
                .andExpect(jsonPath("$[1].name", is(subject_1.getName())))
                .andExpect(jsonPath("$[2].name", is(subject_2.getName())));
    }

    @Test
    void whenGetSubjectByValidId_Status200_ReturnSubject() throws Exception {
        this.mockMvc.perform(get("/api/subjects/{subjectId}", subject_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME, is(SUBJECT_NAME_1)));
    }

    @Test
    void whenAddSubjectWithValidName_Status200() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(SUBJECT_NAME_4);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME, is(SUBJECT_NAME_4)));
    }

    @Test
    void whenAddSubjectWithInvalidLength_Status400() throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Subject's name length cannot be less then 2 and greater then 50!");
        SubjectDTO newSubject = new SubjectDTO(SUBJECT_INVALID_NAME);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(errors)));
    }

    @Test
    void whenAddSubjectWithDuplicateName_Status400() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(SUBJECT_NAME_2);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(String.format("Error! Already exists Subject with name %s",
                        SUBJECT_NAME_2))));
    }

    @Test
    void whenUpdateSubjectWithValidData_Status200() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(subject_2.getId(), SUBJECT_NAME_4);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(subject_2.getId())))
                .andExpect(jsonPath("$.name", is(SUBJECT_NAME_4)));
    }

    @Test
    void whenUpdateSubjectDuplicateName_Status400() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(subject_2.getId(), SUBJECT_NAME_1);
        this.mockMvc.perform(put("/api/subjects/{subjectId}",subject_2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors",
                        is(String.format("Error! Already exists Subject with name %s", SUBJECT_NAME_1))));
    }

    @Test
    void whenDeleteSubjectByValidId_Status200()throws Exception{
        this.mockMvc.perform(delete("/api/subjects/{subjectId}", subject_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteSubjectByInValidId_Status404()throws Exception{
        this.mockMvc.perform(delete("/api/subjects/{subjectId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors", is(String.format("Error! Couldn't find Subject by id %d",INVALID_ID))));
    }

}