package ua.com.nikiforov.services.persons;

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
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.persons.Student;

@SpringJUnitConfig(UniversityConfig.class)
class StudentsServiseImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final long TEST_GROUP_ID_1 = 1;
    private static final long TEST_GROUP_ID_2 = 2;

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
    void afterAddStudentGetStudentByIdReturnCorrectStudentObject() {
        Student expectedStudent = insertStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        assertEquals(expectedStudent, studentService.getStudentById(expectedStudent.getId()));
    }

    @Test
    void whenGetAllStudentsIfPresentReturnListOfAllStudents() {
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1));
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, TEST_GROUP_ID_1));
        expectedStudents.add(insertStudent(FIRST_NAME_3, LAST_NAME_3, TEST_GROUP_ID_1));
        assertIterableEquals(expectedStudents, studentService.getAllStudents());
    }

    @Test
    void whenUpdateStudentIfSuccessThenReturnTrue() {
        long studentId = insertStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1).getId();
        assertTrue(studentService.updateStudent(FIRST_NAME_2, LAST_NAME_2, TEST_GROUP_ID_2, studentId));
    }

    @Test
    void afterUpdateStudentIfSuccessThenGetStudentByIdReturnUpdatedStudent() {
        long studentId = insertStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1).getId();
        studentService.updateStudent(FIRST_NAME_2, LAST_NAME_2, TEST_GROUP_ID_2, studentId);
        Student expectedStudent = studentService.getStudentByNameGroupId(FIRST_NAME_2, LAST_NAME_2, TEST_GROUP_ID_2);
        Student actualStudent = studentService.getStudentById(studentId);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void whenDeleteStudentByIdIfSuccessThenReturnTrue() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        assertTrue(studentService.deleteStudentById(student.getId()));
    }

    @Test
    void afterDeleteStudentByIdIfSearchForItReturnEntityNotFoundException() {
        studentService.addStudent(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        Student student = studentService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, TEST_GROUP_ID_1);
        long studentId = student.getId();
        studentService.deleteStudentById(studentId);
        assertThrows(EntityNotFoundException.class, () -> studentService.getStudentById(studentId));
    }

    private Student insertStudent(String firstName, String lastaName, long groupName) {
        studentService.addStudent(firstName, lastaName, groupName);
        return studentService.getStudentByNameGroupId(firstName, lastaName, groupName);

    }

}
