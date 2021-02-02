package ua.com.nikiforov.rest_controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


class SubjectsRestControllerTest extends SetupTestHelper {

    private static final String NAME = "$.name";
    private static final String SUBJECT_ID = "subject_id";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(value = ADD_THREE_SUBJECTS_XML, cleanAfter = true)
    @ExpectedDataSet(value = ADD_THREE_SUBJECTS_XML, ignoreCols = SUBJECT_ID)
    void whenGetAllSubjects_Status200_ReturnSubjectsList() throws Exception {
        this.mockMvc.perform(get("/api/subjects/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)));

    }

    @Test
    @DataSet(value = ADD_ONE_SUBJECT_XML, executeScriptsBefore = RESET_SUBJECT_ID,
            cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_SUBJECT_XML, ignoreCols = SUBJECT_ID)
    void whenGetSubjectByValidId_Status200_ReturnSubject() throws Exception {
        this.mockMvc.perform(get("/api/subjects/{subjectId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME, is(SUBJECT_NAME_1)));
    }

    @Test
    @DataSet(executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_SUBJECT_XML, ignoreCols = SUBJECT_ID)
    void whenAddSubjectWithValidName_Status200() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(SUBJECT_NAME_1);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME, is(SUBJECT_NAME_1)));
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
    @DataSet(value = ADD_THREE_SUBJECTS_XML, executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
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
    @DataSet(value = ADD_THREE_SUBJECTS_XML, executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
    void whenUpdateSubjectWithValidData_Status200() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(ID_1, SUBJECT_NAME_4);
        this.mockMvc.perform(post("/api/subjects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(NAME, is(SUBJECT_NAME_4)));
    }

    @Test
    @DataSet(value = ADD_THREE_SUBJECTS_XML, executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
    void whenUpdateSubjectDuplicateName_Status400() throws Exception {
        SubjectDTO newSubject = new SubjectDTO(ID_1, SUBJECT_NAME_2);
        this.mockMvc.perform(put("/api/subjects/{subjectId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSubject)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS,
                        is(String.format("Error! Already exists Subject with name %s", SUBJECT_NAME_2))));
    }

    @Test
    @DataSet(value = ADD_ONE_SUBJECT_XML, executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
    void whenDeleteSubjectByValidId_Status200() throws Exception {
        this.mockMvc.perform(delete("/api/subjects/{subjectId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/subjects/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(0)));
    }

    @Test
    @DataSet(value = ADD_ONE_SUBJECT_XML, executeScriptsBefore = RESET_SUBJECT_ID, cleanAfter = true)
    void whenDeleteSubjectByInValidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/subjects/{subjectId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS, is(String.format("Error! Couldn't find Subject by id %d", INVALID_ID))));
    }
}
