package ua.com.nikiforov.services.subject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.persons.TeacherService;

@SpringJUnitConfig(UniversityConfig.class)
class SubjectServiceImplTest {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";
    private static final long SUBJECT_COUNT = 3;

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
        assertTrue(subjectService.addSubject(SUBJECT_NAME_1));
    }

    @Test
    void afetrAddSubjectGetSubjectByIdReturnCorrectSubject() {
        Subject expectedSubject = insertSubject(SUBJECT_NAME_1);
        assertEquals(expectedSubject, subjectService.getSubjectById(expectedSubject.getId()));
    }

    @Test
    void whenGetAllSubjectsIfPresentReturnListOfAllSubjects() {
        List<Subject> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(insertSubject(SUBJECT_NAME_1));
        expectedSubjects.add(insertSubject(SUBJECT_NAME_2));
        expectedSubjects.add(insertSubject(SUBJECT_NAME_3));
        List<Subject> actualSubjects = subjectService.getAllSubjects();
        assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void whenUpdateSubjectByIdIfSuccessThenReturnTrue() {
        Subject subject = insertSubject(SUBJECT_NAME_1);
        assertTrue(subjectService.updateSubject(SUBJECT_NAME_2, subject.getId()));
    }

    @Test
    void whenUpdateSubjectIfSuccessThenGetSubjectByIdAfterUpdateReturnChangedName() {
        Subject subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        subjectService.updateSubject(SUBJECT_NAME_2, subjectId);
        Subject expectedSubject = subjectService.getSubjectByName(SUBJECT_NAME_2);
        Subject actualSubject = subjectService.getSubjectById(subjectId);
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void whenDeleteSubjectByIdIfSuccessThenReturnTrue() {
        Subject subject = insertSubject(SUBJECT_NAME_1);
        assertTrue(subjectService.deleteSubjectById(subject.getId()));
    }

    @Test
    void afterDeleteSubjectByIdIfSearchForItReturnEmptyResultDataAccessException() {
        Subject subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        subjectService.deleteSubjectById(subjectId);
        assertThrows(EmptyResultDataAccessException.class, () -> subjectService.getSubjectById(subjectId));
    }

    @Test
    void afterAssignSubjectsToTeachersSubjectHasListOfTeacherIds() {
        Subject subject = insertSubject(SUBJECT_NAME_1);
        int subjectId = subject.getId();
        Teacher teacherOne = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacherTwo = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        Teacher teacherThree = insertTeacher(FIRST_NAME_3, LAST_NAME_3);
        teacherService.assignSubjectToTeacher(subjectId, teacherOne.getId());
        teacherService.assignSubjectToTeacher(subjectId, teacherTwo.getId());
        teacherService.assignSubjectToTeacher(subjectId, teacherThree.getId());
        subject = subjectService.getSubjectById(subjectId);
        StringBuilder expectedTeachersIds = new StringBuilder();
        expectedTeachersIds.append(teacherOne.getId()).append(teacherTwo.getId()).append(teacherThree.getId());
        StringBuilder actualTeachersIds = new StringBuilder();
        long countTeachersIds = subject.getSubjectTeachersIds().stream().map(s -> actualTeachersIds.append(s)).count();
        assertEquals(expectedTeachersIds.toString(), actualTeachersIds.toString());
        assertEquals(SUBJECT_COUNT, countTeachersIds);
    }

    private Subject insertSubject(String subjectName) {
        subjectService.addSubject(subjectName);
        return subjectService.getSubjectByName(subjectName);
    }

    private Teacher insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(firstName, lastName);
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
