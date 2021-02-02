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


class TeacherRestControllerTest extends SetupTestHelper {

    private static final String TEACHER_ID = "teacher_id";
    private static final String FIRST_NAME = "$.firstName";
    private static final String LAST_NAME = "$.lastName";
    private static final String SUBJECTS = "$.subjects";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(value = ADD_THREE_TEACHERS_XML, cleanAfter = true)
    @ExpectedDataSet(value = ADD_THREE_TEACHERS_XML, ignoreCols = TEACHER_ID)
    void whenGetAllTeachers_Status200_ReturnTeachersList() throws Exception {
        this.mockMvc.perform(get("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)));

    }

    @Test
    @DataSet(value = ADD_ONE_TEACHER_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_TEACHER_XML, ignoreCols = TEACHER_ID)
    void whenGetTeacherByValidId_IfSuccessStatus200_ReturnTeacher() throws Exception {
        this.mockMvc.perform(get("/api/teachers/{teacherId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(TEACHERS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(TEACHERS_LAST_NAME_1)));
    }

    @Test
    @DataSet(executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenAddTeacher_IfSuccessStatus200() throws Exception {
        TeacherDTO newTeacher = new TeacherDTO(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        this.mockMvc.perform(post("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTeacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(TEACHERS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(TEACHERS_LAST_NAME_1)));
    }

    @Test
    @DataSet(value = ADD_THREE_TEACHERS_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenAddTeacherWithDuplicateNames_Status400() throws Exception {
        String errorMessage = String.format(
                "Error! Already exists Teacher with firstName = %s, lastName = %s", TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        TeacherDTO newTeacher = new TeacherDTO(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        this.mockMvc.perform(post("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newTeacher)))
                .andExpect(jsonPath(STATUS, is(400)))
                .andExpect(jsonPath(STATUS, is(errorMessage)));
    }

    @Test
    @DataSet(value = ADD_ONE_TEACHER_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenUpdateTeacher_IfSuccessStatus200() throws Exception {
        TeacherDTO updatedStudent = new TeacherDTO(ID_1, TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_3);
        this.mockMvc.perform(put("/api/teachers/{teacherId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(TEACHERS_FIRST_NAME_2)))
                .andExpect(jsonPath(LAST_NAME, is(TEACHERS_LAST_NAME_3)));
    }

    @Test
    @DataSet(value = ADD_THREE_TEACHERS_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenUpdateTeacherWithDuplicateNames_IfSuccessStatus400() throws Exception {
        String errorMessage = String.format("Error! Already exists Teacher with firstName = %s, lastName = %s", TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        TeacherDTO updatedTeacher = new TeacherDTO(ID_1, TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        this.mockMvc.perform(put("/api/teachers/{teacherId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedTeacher)))
                .andExpect(jsonPath(STATUS, is(400)))
                .andExpect(jsonPath(ERRORS, is(errorMessage)));
    }

    @Test
    @DataSet(value = ADD_ONE_TEACHER_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenDeleteTeacherByValidId_Status200_TeacherDelete() throws Exception {
        this.mockMvc.perform(delete("/api/teachers/{teacherId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/teachers/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(0)));
    }

    @Test
    @DataSet(value = ADD_ONE_TEACHER_XML, executeScriptsBefore = RESET_TEACHER_ID, cleanAfter = true)
    void whenDeleteTeacherWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/teachers/{teacherId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS, is(
                        String.format("Failed to delete Teacher by id %d", INVALID_ID))));

    }

    @Test
    @DataSet(value = {ADD_ONE_TEACHER_XML, ADD_THREE_SUBJECTS_XML},
            executeScriptsBefore = {RESET_TEACHER_ID, RESET_SUBJECT_ID},
            cleanAfter = true)
    void whenAssignTeacherToSubject_Status200_TeacherAssigned() throws Exception {
        List<SubjectDTO> subjects = new ArrayList<>();
        subjects.add(new SubjectDTO(ID_1, SUBJECT_NAME_1));
        this.mockMvc.perform(post("/api/teachers/{teacherId}/{subjectId}", ID_1, ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(TEACHERS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(TEACHERS_LAST_NAME_1)))
                .andExpect(jsonPath(SUBJECTS, hasSize(1)))
                .andExpect(jsonPath("$.subjects[0].id", is(ID_1)))
                .andExpect(jsonPath("$.subjects[0].name", is(SUBJECT_NAME_1)));
    }

    @Test
    @DataSet(value = {ADD_ONE_TEACHER_XML, ADD_THREE_SUBJECTS_XML},
            executeScriptsBefore = {RESET_TEACHER_ID, RESET_SUBJECT_ID},
            cleanAfter = true)
    void whenUnassignTeacherToSubject_Status200_TeacherAssigned() throws Exception {
        teacherService.assignSubjectToTeacher(ID_1, ID_1);
        this.mockMvc.perform(post("/api/teachers/{teacherId}/subject/{subjectId}", ID_1, ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(TEACHERS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(TEACHERS_LAST_NAME_1)))
                .andExpect(jsonPath(SUBJECTS, hasSize(0)));
    }
}
