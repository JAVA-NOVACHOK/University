package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class SubjectsControllerTest {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";
    private static final String SUBJECT_NAME_4 = "Java";
    private static final String SUBJECT_NAME_5 = "WordPress";

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
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;
    private SubjectDTO subject_4;
    private SubjectDTO subject_5;

    private TeacherDTO teacher;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);
        teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
    }

    @Test
    void allSubjects_ReturnsSubjectsView_WithSubjectsAttrs() throws Exception {
        this.mockMvc.perform(get("/subjects/"))
                .andDo(print())
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_5, subject_3)))
                .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void addSubject_SuccessAddSubjects_WithSubjectsAttrs() throws Exception {
        this.mockMvc
        .perform(post("/subjects/add/")
                .param(SUBJECT_NAME_ATTR,SUBJECT_NAME_4))
        .andDo(print())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attributeExists(SUBJECTS_ATTR))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    
    @Test
    void addSubject_IfAddExistingSubject_FailAdding() throws Exception {
        subject_5 = subjectService.getSubjectByName(SUBJECT_NAME_5);
        this.mockMvc
        .perform(post("/subjects/add/")
                .param(SUBJECT_NAME_ATTR,SUBJECT_NAME_1))
        .andDo(print())
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(model().attribute(SUBJECTS_ATTR,hasItems(subject_1,subject_5,subject_3)))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void addSubject_ifAddinExistingSubject_WithSubjectsAttrs() throws Exception {
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
        this.mockMvc
        .perform(get("/subjects/edit/")
                .param(ID_ATTR,subject_1.getId() + STR))
        .andDo(print())
        .andExpect(model().attributeExists(SUBJECT_ATTR))
        .andExpect(view().name(VIEW_SUBJECTS_EDIT_FORM));
    }
    
    @Test
    void editSubjectURIWithSubjectParam_thenReturnSuccessSubject() throws Exception {
        SubjectDTO updatedSubject = new SubjectDTO(subject_2.getId(), SUBJECT_NAME_5,subject_2.getTeachers());
        this.mockMvc
        .perform(post("/subjects/edit/")
                .param(ID_ATTR,subject_2.getId() + STR)
                .param(NAME_ATTR, SUBJECT_NAME_5)
                .sessionAttr(SUBJECT_ATTR, new SubjectDTO()))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1,updatedSubject,subject_3)))
        .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void deleteSubjectURIWithSubjectIdParam_thenReturnSuccessDelete() throws Exception {
        SubjectDTO subject_4 = subjectService.getSubjectByName(SUBJECT_NAME_4);
        this.mockMvc
            .perform(get("/subjects/delete")
                    .param(ID_ATTR, subject_4.getId() + STR))
            .andExpect(status().isOk())
            .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1,subject_2,subject_3)))
            .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void assignSubjectToTeacher_thenReturnSuccessAsignedSubject() throws Exception {
        subject_1.addTeacher(teacher);
        this.mockMvc
            .perform(post("/subjects/assign/")
                    .param(SUBJECT_ID_ATTR, subject_1.getId() + STR)
                    .param(TEACHER_ID_ATTR, teacher.getId() + STR))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists(SUCCESS_MSG))
            .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1,subject_5,subject_3)))
            .andExpect(view().name(SUBJECTS_VIEW));
    }
    
    @Test
    void unassignSubjectFromTeacher_thenReturnSuccessMSG() throws Exception {
        this.mockMvc
        .perform(get("/subjects/unassign/")
                .param(SUBJECT_ID_ATTR, subject_1.getId() + STR)
                .param(TEACHER_ID_ATTR, teacher.getId() + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1,subject_2,subject_3)))
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
