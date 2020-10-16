package ua.com.nikiforov.services.persons;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.persons.Student;

@SpringJUnitConfig(UniversityConfig.class)
class StudentsServiseImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String SPACE = " ";
    private static final String NEW_LINE = System.lineSeparator();

    private static final long TEST_GROUP_ID_1 = 1;
    private static final long TEST_GROUP_ID_2 = 2;

    private static final int STUDENT_TEST_COUNT = 3;

    @Autowired
    private StudentsService studentService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddStudentIfSuccessReturnTrue() {
        assertTrue(studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1));
    }

    @Test
    void whenGetStudentByNameReturnCorrectStudentObject() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        assertEquals(FIRST_NAME_1, student.getFirstName());
        assertEquals(LAST_NAME_1, student.getLastName());
        assertEquals(TEST_GROUP_ID_1, student.getGroupId());
    }

    @Test
    void whenGetAllStudentsIfPresentReturnListOfAllStudents() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        studentService.addStudent(FIRST_NAME_2, LAST_NAME_2, TEST_GROUP_ID_1);
        studentService.addStudent(FIRST_NAME_3, LAST_NAME_3, TEST_GROUP_ID_1);
        StringBuilder expectedNames = new StringBuilder();
        expectedNames.append(FIRST_NAME_1).append(SPACE).append(LAST_NAME_1).append(SPACE).append(TEST_GROUP_ID_1)
                .append(NEW_LINE);
        expectedNames.append(FIRST_NAME_2).append(SPACE).append(LAST_NAME_2).append(SPACE).append(TEST_GROUP_ID_1)
                .append(NEW_LINE);
        expectedNames.append(FIRST_NAME_3).append(SPACE).append(LAST_NAME_3).append(SPACE).append(TEST_GROUP_ID_1)
                .append(NEW_LINE);
        StringBuilder actualNames = new StringBuilder();
        long countNames = studentService.getAllStudents().stream().map(s -> actualNames.append(s.getFirstName()).append(SPACE)
                .append(s.getLastName()).append(SPACE).append(s.getGroupId()).append(NEW_LINE)).count();
        assertEquals(expectedNames.toString(), actualNames.toString());
        assertEquals(STUDENT_TEST_COUNT, countNames);
    }

    @Test
    void whenUpdateStudentsFirstNameIfSuccessThenReturnTrue() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        assertTrue(studentService.updateStudent(FIRST_NAME_2, LAST_NAME_1, TEST_GROUP_ID_1, studentId));
    }

    @Test
    void whenUpdateStudentsLastNameIfSuccessThenReturnTrue() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        assertTrue(studentService.updateStudent(FIRST_NAME_1, LAST_NAME_2, TEST_GROUP_ID_1, studentId));
    }

    @Test
    void whenUpdateStudentsGroupIdIfSuccessThenReturnTrue() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        assertTrue(studentService.updateStudent(FIRST_NAME_1, LAST_NAME_2, TEST_GROUP_ID_2, studentId));
    }

    @Test
    void afterUpdateStudentsFirstNameIfSuccessThenGetStudentByIdReturnChangedFirstName() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        studentService.updateStudent(FIRST_NAME_2, LAST_NAME_1, TEST_GROUP_ID_1, studentId);
        assertEquals(FIRST_NAME_2, studentService.getStudentById(studentId).getFirstName());
    }

    @Test
    void afterUpdateStudentsLastNameIfSuccessThenGetStudentByIdReturnChangedLastName() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        studentService.updateStudent(FIRST_NAME_1, LAST_NAME_2, TEST_GROUP_ID_1, studentId);
        assertEquals(LAST_NAME_2, studentService.getStudentById(studentId).getLastName());
    }

    @Test
    void afterUpdateStudentsGroupIdIfSuccessThenGetStudentsByIdReturnChangedGroupId() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        studentService.updateStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_2, studentId);
        assertEquals(TEST_GROUP_ID_2, studentService.getStudentById(studentId).getGroupId());
    }

    @Test
    void whenDeleteStudentByIdIfSuccessThenReturnTrue() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        assertTrue(studentService.deleteStudentById(student.getId()));
    }

    @Test
    void afterDeleteStudentByIdIfSearchForItReturnEmptyResultDataAccessException() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        studentService.deleteStudentById(studentId);
        assertThrows(EmptyResultDataAccessException.class, () -> studentService.getStudentById(studentId));
    }

}
