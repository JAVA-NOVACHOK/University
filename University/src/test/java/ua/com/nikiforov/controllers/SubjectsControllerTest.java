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
import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class, TestDataSource.class })
@WebAppConfiguration
class SubjectsControllerTest {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";
    
    private static final String SUBJECT_NAME_ATTR = "subjectName";
    private static final String NAME_ATTR = "name";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String SUBJECT_ATTR = "subject";
    private static final String ID_ATTR = "id";
    private static final String SUBJECT_ID_ATTR = "subjectId";
    private static final String TEACHER_ID_ATTR = "teacherId";
    
    private static final String SUBJECTS_VIEW = "subjects/subjects";
    private static final String VIEW_SUBJECTS_EDIT_FORM = "subjects/edit_subject_form";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";
    
    private static final String FIRST_NAME_1 = "Tom";
    private static final String LAST_NAME_1 = "Hanks";

    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TableCreator tableCreator;

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
    void allSubjects_ReturnsSubjectsView_WithSubjectsAttrs() throws Exception {
        SubjectDTO subject_1 = insertSubject(SUBJECT_NAME_1);
        SubjectDTO subject_2 = insertSubject(SUBJECT_NAME_2);
        SubjectDTO subject_3 = insertSubject(SUBJECT_NAME_3);
        this.mockMvc.perform(get("/subjects/"))
                .andDo(print())
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void addSubject_SuccessAddSubjects_WithSubjectsAttrs() throws Exception {
        this.mockMvc
        .perform(post("/subjects/add/")
                .param(SUBJECT_NAME_ATTR,SUBJECT_NAME_1))
        .andDo(print())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attributeExists(SUBJECTS_ATTR))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    
    @Test
    void addSubject_IfAddExistingSubject_FailAdding() throws Exception {
       SubjectDTO subject_1 = insertSubject(SUBJECT_NAME_1);
       SubjectDTO subject_2 = insertSubject(SUBJECT_NAME_2);
       SubjectDTO subject_3 = insertSubject(SUBJECT_NAME_3);
        
        this.mockMvc
        .perform(post("/subjects/add/")
                .param(SUBJECT_NAME_ATTR,SUBJECT_NAME_1))
        .andDo(print())
        .andExpect(model().attribute(SUBJECTS_ATTR,hasItems(subject_1,subject_2,subject_3)))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void addSubject_ifAddinExistingSubject_WithSubjectsAttrs() throws Exception {
        SubjectDTO subject_1 = insertSubject(SUBJECT_NAME_1);
        SubjectDTO subject_2 = insertSubject(SUBJECT_NAME_2);
        SubjectDTO subject_3 = insertSubject(SUBJECT_NAME_3);
        this.mockMvc
        .perform(post("/subjects/add/")
                .param(SUBJECT_NAME_ATTR,SUBJECT_NAME_1))
        .andDo(print())
        .andExpect(model().attribute(SUBJECTS_ATTR,hasItems(subject_1,subject_2,subject_3)))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void editSubjectURI_thenReturnViewEditForm() throws Exception {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        this.mockMvc
        .perform(get("/subjects/edit/")
                .param(ID_ATTR,subject.getId() + STR))
        .andDo(print())
        .andExpect(model().attributeExists(SUBJECT_ATTR))
        .andExpect(view().name(VIEW_SUBJECTS_EDIT_FORM));
    }
    
    @Test
    void editSubjectURIWithSubjectParam_thenReturnSuccessSubject() throws Exception {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        SubjectDTO updatedSubject = new SubjectDTO(subject.getId(), SUBJECT_NAME_2);
        this.mockMvc
        .perform(post("/subjects/edit/")
                .param(ID_ATTR,subject.getId() + STR)
                .param(NAME_ATTR, SUBJECT_NAME_2)
                .sessionAttr(SUBJECT_ATTR, new Subject()))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(updatedSubject)))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void deleteSubjectURIWithSubjectIdParam_thenReturnSuccessDelete() throws Exception {
        SubjectDTO subject_1 = insertSubject(SUBJECT_NAME_1);
        SubjectDTO subject_2 = insertSubject(SUBJECT_NAME_2);
        SubjectDTO subject_3 = insertSubject(SUBJECT_NAME_3);
        this.mockMvc
            .perform(get("/subjects/delete")
                    .param(ID_ATTR, subject_1.getId() + STR))
            .andExpect(status().isOk())
            .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_2,subject_3)))
            .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void assignSubjectToTeacher_thenReturnSuccessAsignedSubject() throws Exception {
        TeacherDTO teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        subject.addTeacher(teacher);
        this.mockMvc
            .perform(post("/subjects/assign/")
                    .param(SUBJECT_ID_ATTR, subject.getId() + STR)
                    .param(TEACHER_ID_ATTR, teacher.getId() + STR))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists(SUCCESS_MSG))
            .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject)))
            .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void unassignSubjectFromTeacher_thenReturnSuccessMSG() throws Exception {
        TeacherDTO teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        teacherService.assignSubjectToTeacher(subject.getId(), teacher.getId());
        this.mockMvc
        .perform(get("/subjects/unassign/")
                .param(SUBJECT_ID_ATTR, subject.getId() + STR)
                .param(TEACHER_ID_ATTR, teacher.getId() + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject)))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    

    private SubjectDTO insertSubject(String subjectName) {
        subjectService.addSubject(subjectName);
        return subjectService.getSubjectByName(subjectName);
    }
    
    private TeacherDTO insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(new TeacherDTO(firstName, lastName));
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
