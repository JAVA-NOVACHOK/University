package ua.com.nikiforov.services.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.subject.SubjectService;

@SpringJUnitConfig(UniversityConfig.class)
class TeacherServiceImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

    private static final String SPACE = " ";
    private static final String NEW_LINE = System.lineSeparator();

    private static final int TEACHER_TEST_COUNT = 3;
    private static final int TEACHERS_SUBJECTS_COUNT = 3;

    @Autowired
    private TeachersService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTEacherIfSuccessReturnTrue() {
        assertTrue(teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1));
    }

    @Test
    void whenGetTeacherByNameReturnCorrectTeacherObject() {

        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        assertEquals(FIRST_NAME_1, teacher.getFirstName());
        assertEquals(LAST_NAME_1, teacher.getLastName());
    }

    @Test
    void whenGetAllTeachersIfPresentReturnListOfAllTeachers() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        teacherService.addTeacher(FIRST_NAME_2, LAST_NAME_2);
        teacherService.addTeacher(FIRST_NAME_3, LAST_NAME_3);
        StringBuilder expected = new StringBuilder();
        expected.append(FIRST_NAME_1).append(SPACE).append(LAST_NAME_1).append(NEW_LINE);
        expected.append(FIRST_NAME_2).append(SPACE).append(LAST_NAME_2).append(NEW_LINE);
        expected.append(FIRST_NAME_3).append(SPACE).append(LAST_NAME_3).append(NEW_LINE);
        StringBuilder actual = new StringBuilder();
        long count = teacherService.getAllTeachers().stream()
                .map(t -> actual.append(t.getFirstName()).append(SPACE).append(t.getLastName()).append(NEW_LINE))
                .count();
        assertEquals(expected.toString(), actual.toString());
        assertEquals(TEACHER_TEST_COUNT, count);
    }

    @Test
    void whenUpdateTeachersFirstNameIfSuccessThenReturnTrue() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        assertTrue(teacherService.updateTeacher(FIRST_NAME_2, LAST_NAME_1, teacherId));
    }

    @Test
    void whenUpdateTeachersLastNameIfSuccessThenReturnTrue() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        assertTrue(teacherService.updateTeacher(FIRST_NAME_1, LAST_NAME_2, teacherId));
    }

    @Test
    void afterUpdateTeachersFirstNameIfSuccessThenGetTeacherByIdReturnChangedFirstName() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.updateTeacher(FIRST_NAME_2, LAST_NAME_1, teacherId);
        assertEquals(FIRST_NAME_2, teacherService.getTeacherById(teacherId).getFirstName());
    }

    @Test
    void afterUpdateTeachersLastNameIfSuccessThenGetTeacherByIdReturnChangedLastName() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.updateTeacher(FIRST_NAME_1, LAST_NAME_2, teacherId);
        assertEquals(LAST_NAME_2, teacherService.getTeacherById(teacherId).getLastName());
    }

    @Test
    void whenDeleteTeacherByIdIfSuccessThenReturnTrue() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        assertTrue(teacherService.deleteTeacherById(teacher.getId()));
    }

    @Test
    void afterDeleteTeacherByIdIfSearchForItReturnEmptyResultDataAccessException() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.deleteTeacherById(teacherId);
        assertThrows(EmptyResultDataAccessException.class, () -> teacherService.getTeacherById(teacherId));
    }

    @Test
    void whenAssignSubjectToTeacherIfSuccessReturnTrue() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        subjectService.addSubject(SUBJECT_NAME_1);
        assertTrue(teacherService.assignSubjectToTeacher(1, 1));
    }

    @Test
    void afterAssignSubjectsToTeacherTeacherHasListOfSubjectIds() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        Subject subjectOne = insertSubject(SUBJECT_NAME_1);
        int subjectOneId = subjectOne.getId();
        Subject subjectTwo = insertSubject(SUBJECT_NAME_2);
        int subjectTwoId = subjectTwo.getId();
        Subject subjectThree = insertSubject(SUBJECT_NAME_3);
        int subjectThreeId = subjectThree.getId();
        teacherService.assignSubjectToTeacher(subjectOneId, teacherId);
        teacherService.assignSubjectToTeacher(subjectTwoId, teacherId);
        teacherService.assignSubjectToTeacher(subjectThreeId, teacherId);
        teacher = teacherService.getTeacherById(teacherId);
        String expectedSubjectIds = subjectOneId + SPACE + subjectTwoId + SPACE + subjectThreeId + SPACE;
        StringBuilder actualSubjectIds = new StringBuilder();
        long countSubjects = teacher.getSubjectIds().stream().map(i -> actualSubjectIds.append(i).append(SPACE)).count();
        assertEquals(expectedSubjectIds, actualSubjectIds.toString());
        assertEquals(TEACHERS_SUBJECTS_COUNT, countSubjects);
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
