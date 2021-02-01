package ua.com.nikiforov.rest_controllers.db_rider_test;

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

class StudentsRestControllerTestDBRider extends SetupTestHelper {

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
                .andExpect(jsonPath("$[0].firstName", is(FIRST_NAME_2)))
                .andExpect(jsonPath("$[0].lastName", is(LAST_NAME_2)))
                .andExpect(jsonPath("$[1].firstName", is(FIRST_NAME_1)))
                .andExpect(jsonPath("$[1].lastName", is(LAST_NAME_1)))
                .andExpect(jsonPath("$[2].firstName", is(FIRST_NAME_3)))
                .andExpect(jsonPath("$[2].lastName", is(LAST_NAME_3)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenGetStudentByValidId_IfSuccessStatus200_ReturnStudent() throws Exception {
        this.mockMvc.perform(get("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_1)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_1)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenAddStudent_IfSuccessStatus200() throws Exception {
        StudentDTO newStudent = new StudentDTO(FIRST_NAME_1, LAST_NAME_1, ID_1);
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_1)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_1)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},disableConstraints = true, cleanAfter = true)
    void whenAddStudentWithDuplicateNames_Status400() throws Exception {
        String errorMessage = "Error! Already exists Student with firstName = Tom, lastName = Hanks, groupId = 1";
        StudentDTO newStudent = new StudentDTO(FIRST_NAME_1, LAST_NAME_1, ID_1);
        this.mockMvc.perform(post("/api/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", is(errorMessage)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_ONE_STUDENT_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_STUDENT_XML, ignoreCols = "student_id")
    void whenUpdateStudent_IfSuccessStatus200() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(ID_1, FIRST_NAME_3, LAST_NAME_4, ID_1);
        this.mockMvc.perform(put("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME_3)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME_4)));
    }

    @Test
    @DataSet(value = {ADD_ONE_GROUP_XML, ADD_THREE_STUDENTS_XML}, executeScriptsBefore = {RESET_STUDENT_ID, RESET_GROUP_ID},
            disableConstraints = true, cleanAfter = true)
    void whenUpdateStudentWithDuplicateNames_IfSuccessStatus400() throws Exception {
        StudentDTO updatedStudent = new StudentDTO(ID_1, FIRST_NAME_2, LAST_NAME_2, ID_1);
        this.mockMvc.perform(put("/api/students/{studentId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", is("Error! Already exists Student with firstName = Bill, lastName = Clinton, groupId = 1")));
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
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

    }

}