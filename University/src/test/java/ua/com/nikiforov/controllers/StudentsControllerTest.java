package ua.com.nikiforov.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private static final String GROUP_ID_ATTR = "groupId";
    private static final String STUDENT_ATTR = "student";
    private static final String ID = "id";
    
    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_TRANSFER_FORM = "students/transfer_form";
    private static final String VIEW_EDIT_FORM = "students/edit_form";
    private static final String VIEW_ADD_FORM = "students/add_student_form";

    private static final String GROUP_ATTR = "group";
    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUPS_ATTR = "groups";
    private static final String STUDENTS_ATTR = "students";
    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";


    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";



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
    void givenStudentURI_thenReturnStudentView_WithGroupAttrs() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        Student student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroup_1.getGroupId());
        Student student_2 = insertStudent(FIRST_NAME_2, LAST_NAME_2, testGroup_1.getGroupId());
        Student student_3 = insertStudent(FIRST_NAME_3, LAST_NAME_3, testGroup_1.getGroupId());
        this.mockMvc.perform(get("/students").param(GROUP_ID_ATTR, testGroup_1.getGroupId() + ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("groupIn", testGroup_1))
                .andExpect(model().size(3))
                .andExpect(view().name(VIEW_STUDENTS));
    }
    
    @Test
    void givenStudentGetEditURI_ReturnStudentEditForm() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        Student student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroup_1.getGroupId());
        this.mockMvc.perform(get("/students/edit").param(ID, student_1.getId() + ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute(STUDENT_ATTR, student_1))
                .andExpect(model().size(2))
                .andExpect(view().name(VIEW_EDIT_FORM));
    }
    
    @Test
    void givenStudentPostEditURI_EditStudent() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        Student student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroup_1.getGroupId());
        this.mockMvc
            .perform(post("/students/edit")
                    .param(ID, student_1.getId() + "")
                    .param(FIRST_NAME_ATTR, FIRST_NAME_1)
                    .param(LAST_NAME_ATTR, LAST_NAME_2)
                    .param(GROUP_ID_ATTR, testGroup_1.getGroupId() + STR))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().size(4))
            .andExpect(model().attributeExists(GROUPS_ATTR))
            .andExpect(model().attributeExists(GROUP_IN_ATTR))
            .andExpect(model().attributeExists(SUCCESS_MSG))
            .andExpect(view().name(VIEW_STUDENTS));
    }
    
    @Test
    void givenStudentPostAddURI_AddStudent() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        this.mockMvc
        .perform(post("/students/add")
                .param(FIRST_NAME_ATTR, FIRST_NAME_1)
                .param(LAST_NAME_ATTR, LAST_NAME_2)
                .param(GROUP_ID_ATTR, testGroup_1.getGroupId() + STR))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().size(4))
        .andExpect(model().attributeExists(GROUPS_ATTR))
        .andExpect(model().attributeExists(GROUP_IN_ATTR))
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(view().name(VIEW_STUDENTS));
    }
    
    @Test
    void givenStudentPostAddURI_IfAddExistingStudent_FailMSGAttr() throws Exception {
        Group testGroup_1 = insertGroup(TEST_GROUP_NAME_1);
        insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroup_1.getGroupId());
        this.mockMvc
        .perform(post("/students/add")
                .param(FIRST_NAME_ATTR, FIRST_NAME_1)
                .param(LAST_NAME_ATTR, LAST_NAME_1)
                .param(GROUP_ID_ATTR, testGroup_1.getGroupId() + STR))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().size(4))
        .andExpect(model().attributeExists(GROUPS_ATTR))
        .andExpect(model().attributeExists(GROUP_IN_ATTR))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_STUDENTS));
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
