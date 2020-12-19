package ua.com.nikiforov.services.subject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.persons.TeacherService;

@SpringJUnitConfig(TestDataSource.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class SubjectServiceImplTest {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddSubjectIfSuccessReturnTrue() {
        assertDoesNotThrow(() -> subjectService.addSubject(SUBJECT_NAME_1));
    }

    @Test
    void afetrAddSubjectGetSubjectByIdReturnCorrectSubject() {
        SubjectDTO expectedSubject = insertSubject(SUBJECT_NAME_1);
        assertEquals(expectedSubject, subjectService.getSubjectById(expectedSubject.getId()));
    }

    @Test
    void whenGetAllSubjectsIfPresentReturnListOfAllSubjects() {
        List<SubjectDTO> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(insertSubject(SUBJECT_NAME_3));
        expectedSubjects.add(insertSubject(SUBJECT_NAME_1));
        expectedSubjects.add(insertSubject(SUBJECT_NAME_2));
        List<SubjectDTO> actualSubjects = subjectService.getAllSubjects();
        assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void whenUpdateSubjectByIdIfSuccessThenReturnTrue() {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        assertDoesNotThrow(() -> subjectService.updateSubject(new SubjectDTO(subject.getId(),SUBJECT_NAME_2)));
    }

    @Test
    void whenUpdateSubjectThenSubjectHasUpdatedName() {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        subjectService.updateSubject(new SubjectDTO(subjectId,SUBJECT_NAME_2));
        SubjectDTO expectedSubject = subjectService.getSubjectByName(SUBJECT_NAME_2);
        SubjectDTO actualSubject = subjectService.getSubjectById(subjectId);
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void whenDeleteSubjectByIdIfSuccessThenReturnTrue() {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        assertDoesNotThrow(() -> subjectService.deleteSubjectById(subject.getId()));
    }

    @Test
    void afterDeleteSubjectByIdIfSearchForItReturnEmptyResultDataAccessException() {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        subjectService.deleteSubjectById(subjectId);
        assertThrows(EntityNotFoundException.class, () -> subjectService.getSubjectById(subjectId));
    }

    @Test
    void afterAssignSubjectsToTeachers_SubjectHasListOfTeachers() {
        SubjectDTO subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        
        List<TeacherDTO> expectedTeachers = new ArrayList<>();
        TeacherDTO teacherTwo = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        TeacherDTO teacherOne = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        TeacherDTO teacherThree = insertTeacher(FIRST_NAME_3, LAST_NAME_3);
        
        expectedTeachers.add(teacherOne);
        expectedTeachers.add(teacherTwo);
        expectedTeachers.add(teacherThree);

        teacherService.assignSubjectToTeacher(teacherOne.getId(),subjectId);
        teacherService.assignSubjectToTeacher(teacherTwo.getId(),subjectId);
        teacherService.assignSubjectToTeacher(teacherThree.getId(),subjectId);
        
        subject = subjectService.getSubjectById(subjectId);
        List<TeacherDTO> actualTeachers = subject.getTeachers();
        assertIterableEquals(expectedTeachers, actualTeachers);
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
