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
import ua.com.nikiforov.dao.tablecreator.TableCreator;
import ua.com.nikiforov.models.persons.Teacher;

@SpringJUnitConfig(UniversityConfig.class)
class TeacherServiceImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String SPACE = " ";
    private static final String NEW_LINE = System.lineSeparator();

    private static final int TEACHER_TEST_COUNT = 3;

    @Autowired
    private TeachersService teacherService;

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
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
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
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        assertTrue(teacherService.updateTeacher(FIRST_NAME_2, LAST_NAME_1, teacherId));
    }

    @Test
    void whenUpdateTeachersLastNameIfSuccessThenReturnTrue() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        assertTrue(teacherService.updateTeacher(FIRST_NAME_1, LAST_NAME_2, teacherId));
    }

    @Test
    void afterUpdateTeachersFirstNameIfSuccessThenGetTeacherByIdReturnChangedFirstName() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.updateTeacher(FIRST_NAME_2, LAST_NAME_1, teacherId);
        assertEquals(FIRST_NAME_2, teacherService.getTeacherById(teacherId).getFirstName());
    }

    @Test
    void afterUpdateTeachersLastNameIfSuccessThenGetTeacherByIdReturnChangedLastName() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.updateTeacher(FIRST_NAME_1, LAST_NAME_2, teacherId);
        assertEquals(LAST_NAME_2, teacherService.getTeacherById(teacherId).getLastName());
    }
    
    @Test
    void whenDeleteTeacherByIdIfSuccessThenReturnTrue() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        assertTrue(teacherService.deleteTeacherById(teacher.getId()));
    }
    
    @Test
    void afterDeleteTeacherByIdIfSearchForItReturnEmptyResultDataAccessException() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        Teacher teacher = teacherService.getTeacherByName(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.deleteTeacherById(teacherId);
        assertThrows(EmptyResultDataAccessException.class, () -> teacherService.getTeacherById(teacherId));
    }

}
