package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class TeacherServiceImplTest extends SetupTestHelper {

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

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;

    @BeforeAll
    void startup() {

        teacher_1 = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        teacher_2 = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        teacher_3 = insertTeacher(FIRST_NAME_3, LAST_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);

    }

    @Test
    void whenGetAllTeachersReturnListOfAllTeachers() {
        List<TeacherDTO> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teacher_2);
        expectedTeachers.add(teacher_1);
        expectedTeachers.add(teacher_3);
        assertIterableEquals(expectedTeachers, teacherService.getAllTeachers());
    }

    @Test
    void whenAddTEacherIfSuccessReturnTrue() {
        assertDoesNotThrow(() -> teacherService.addTeacher(new TeacherDTO(FIRST_NAME_1, LAST_NAME_2)));
    }

    @Test
    void afterAddTeacherReturnCorrectTeacherObject() {
        assertEquals(teacher_1, teacherService.getTeacherById(teacher_1.getId()));
    }


    @Test
    void whenUpdateTeacherIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> teacherService.updateTeacher(new TeacherDTO(teacher_2.getId(), FIRST_NAME_2, LAST_NAME_1)));
    }

    @Test
    void afterUpdateTeacherThenTeacherHasUpdatedTeacher() {
        TeacherDTO updatedTeacher = new TeacherDTO(teacher_2.getId(), FIRST_NAME_2, LAST_NAME_2);
        teacherService.updateTeacher(updatedTeacher);
        TeacherDTO actualTeacher = teacherService.getTeacherById(teacher_2.getId());
        assertEquals(updatedTeacher, actualTeacher);
    }

    @Test
    void whenDeleteTeacherByIdIfSuccessNoException() {
        assertDoesNotThrow(() -> teacherService.deleteTeacherById(teacher_2.getId()));
    }

    @Test
    void afterDeleteTeacherByIdIfSearchReturnEntityNotFoundException() {
        teacherService.deleteTeacherById(teacher_3.getId());
        assertThrows(EntityNotFoundException.class, () -> teacherService.getTeacherById(teacher_3.getId()));
    }

    @Test
    void whenAssignSubjectToTeacherIfSuccessNoException() {
        assertDoesNotThrow(() -> teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_1.getId()));
    }

    @Test
    void afterAssignSubjectsToTeacher_ItHasListOfSubjects() {

        List<SubjectDTO> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subject_1);
        expectedSubjects.add(subject_2);
        expectedSubjects.add(subject_3);

        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_2.getId());
        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_3.getId());

        TeacherDTO teacherAfterAssignSubjects = teacherService.getTeacherById(teacher_1.getId());

        List<SubjectDTO> actualSubjects = teacherAfterAssignSubjects.getSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void afterUnAssignSubjectsFromTeacher_HeHasNoSubjectInListOfSubjects() {
        List<SubjectDTO> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subject_2);
        expectedSubjects.add(subject_3);
        teacherService.unassignSubjectFromTeacher(teacher_1.getId(), subject_1.getId());

        TeacherDTO teacherAfterUnassignSubjects = teacherService.getTeacherById(teacher_1.getId());
        List<SubjectDTO> actualSubjects = teacherAfterUnassignSubjects.getSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

}
