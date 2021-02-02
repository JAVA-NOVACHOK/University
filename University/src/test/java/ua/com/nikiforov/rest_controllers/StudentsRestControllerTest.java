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
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentsRestControllerTest extends SetupTestHelper {

    private static final String FIRST_NAME = "$.firstName";
    private static final String LAST_NAME = "$.lastName";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_THREE_STUDENTS_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},disableConstraints = true, cleanAfter = true)
    void whenGetAllStudents_Status200_ReturnStudentsList() throws Exception {
        this.mockMvc.perform(get("/api/students/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(STUDENTS_FIRST_NAME_2)))
                .andExpect(jsonPath("$[0].lastName", is(STUDENTS_LAST_NAME_2)))
                .andExpect(jsonPath("$[1].firstName", is(STUDENTS_FIRST_NAME_1)))
                .andExpect(jsonPath("$[1].lastName", is(STUDENTS_LAST_NAME_1)))
                .andExpect(jsonPath("$[2].firstName", is(STUDENTS_FIRST_NAME_3)))
                .andExpect(jsonPath("$[2].lastName", is(STUDENTS_LAST_NAME_3)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenGetStudentByValidId_IfSuccessStatus200_ReturnStudent() throws Exception {
        this.mockMvc.perform(get("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(STUDENTS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(STUDENTS_LAST_NAME_1)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenAddStudent_IfSuccessStatus200() throws Exception {
        StudentDTO newStudent = new StudentDTO(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, ID_1);
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(STUDENTS_FIRST_NAME_1)))
                .andExpect(jsonPath(LAST_NAME, is(STUDENTS_LAST_NAME_1)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},disableConstraints = true, cleanAfter = true)
    void whenAddStudentWithDuplicateNames_Status400() throws Exception {
        String errorMessage = "Error! Already exists Student with firstName = Tom, lastName = Hanks, groupId = 1";
        StudentDTO newStudent = new StudentDTO(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, ID_1);
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(errorMessage)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenUpdateStudent_IfSuccessStatus200() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(ID_1, STUDENTS_FIRST_NAME_3, STUDENTS_LAST_NAME_2, ID_1);
        this.mockMvc.perform(put("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(STUDENTS_FIRST_NAME_3)))
                .andExpect(jsonPath(LAST_NAME, is(STUDENTS_LAST_NAME_2)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_THREE_STUDENTS_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    void whenUpdateStudentWithDuplicateNames_IfSuccessStatus400() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(ID_1, STUDENTS_FIRST_NAME_2, STUDENTS_LAST_NAME_2, ID_1);
        this.mockMvc.perform(put("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is("Error! Already exists Student with firstName = Bill, lastName = Clinton, groupId = 1")));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    void whenDeleteStudentByValidId_Status200_StudentDelete() throws Exception {
        this.mockMvc.perform(delete("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/students/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(0)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, disableConstraints = true, cleanAfter = true)
    void whenDeleteStudentWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/students/{studentId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()));

    }
}
