package ua.com.nikiforov.services.persons;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.DatabaseConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;

@SpringJUnitConfig(DatabaseConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class StudentsServiseImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    
    @Autowired
    private TableCreator tableCreator;
    
    @Autowired
    private StudentsService studentsService;

    @Autowired
    private GroupService groupService;

    private long testGroupName_1;
    private long testGroupName_2;

    @BeforeEach
    void init() {
        tableCreator.createTables();
        testGroupName_1 = insertGroup(TEST_GROUP_NAME_1).getGroupId();
        testGroupName_2 = insertGroup(TEST_GROUP_NAME_2).getGroupId();
    }

    @Test
    void whenAddStudentIfSuccessReturnTrue() {
        assertTrue(studentsService.addStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1));
    }

    @Test
    void afterAddStudentReturnCorrectStudentObject() {
        Student expectedStudent = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1);
        assertEquals(expectedStudent, studentsService.getStudentById(expectedStudent.getGroupId()));
    }

    @Test
    void whenGetAllStudentsIfPresentReturnListOfAllStudents() {
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1));
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, testGroupName_1));
        expectedStudents.add(insertStudent(FIRST_NAME_3, LAST_NAME_3, testGroupName_1));
        Collections.sort(expectedStudents);
        assertIterableEquals(expectedStudents, studentsService.getAllStudents());
    }

    @Test
    void whenUpdateStudentIfSuccessThenReturnTrue() {
        long studentId = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1).getGroupId();
        assertTrue(studentsService.updateStudent(new Student(studentId,FIRST_NAME_2, LAST_NAME_2, testGroupName_2)));
    }

    @Test
    void afterUpdateStudentIfSuccessThenGetStudentByIdReturnUpdatedStudent() {
        long studentId = insertStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1).getGroupId();
        studentsService.updateStudent(new Student( studentId,FIRST_NAME_2, LAST_NAME_2, testGroupName_2));
        Student expectedStudent = studentsService.getStudentByNameGroupId(FIRST_NAME_2, LAST_NAME_2, testGroupName_2);
        Student actualStudent = studentsService.getStudentById(studentId);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void whenDeleteStudentByIdIfSuccessThenReturnTrue() {
        studentsService.addStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1);
        Student student = studentsService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, testGroupName_1);
        assertTrue(studentsService.deleteStudentById(student.getGroupId()));
    }

    @Test
    void afterDeleteStudentByIdIfSearchReturnEntityNotFoundException() {
        studentsService.addStudent(FIRST_NAME_1, LAST_NAME_1, testGroupName_1);
        Student student = studentsService.getStudentByNameGroupId(FIRST_NAME_1, LAST_NAME_1, testGroupName_1);
        long studentId = student.getGroupId();
        studentsService.deleteStudentById(studentId);
        assertThrows(EntityNotFoundException.class, () -> studentsService.getStudentById(studentId));
    }

    private Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    public Student insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(firstName, lastaName, groupName);
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
    }

}
