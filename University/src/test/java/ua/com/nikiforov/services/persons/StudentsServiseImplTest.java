package ua.com.nikiforov.services.persons;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.services.group.GroupService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class StudentsServiceImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Frank";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Bird";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";


    @Autowired
    private StudentsService studentsService;

    @Autowired
    private GroupService groupService;

    private GroupDTO group_1;

    private StudentDTO student_1;
    private StudentDTO student_2;
    private StudentDTO student_3;

    @BeforeAll
    void init() {
        group_1 = insertGroup(TEST_GROUP_NAME_1);

        student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        student_2 = insertStudent(FIRST_NAME_2, LAST_NAME_2, group_1.getGroupId());
        student_3 = insertStudent(FIRST_NAME_3, LAST_NAME_3, group_1.getGroupId());
    }

    @Test
    @Order(1)
    void whenGetAllStudentsIfPresentReturnListOfAllStudents() {
        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(student_2);
        expectedStudents.add(student_1);
        expectedStudents.add(student_3);
        assertIterableEquals(expectedStudents, studentsService.getAllStudents());
    }

    @Test
    @Order(2)
    void whenAddStudentIfSuccessReturnTrue() {
        assertDoesNotThrow(() -> studentsService.addStudent(new StudentDTO(FIRST_NAME_4, LAST_NAME_4, group_1.getGroupId())));
    }

    @Test
    @Order(3)
    void afterAddStudentReturnCorrectStudentObject() {
        assertEquals(student_1, studentsService.getStudentById(student_1.getId()));
    }


    @Test
    @Order(4)
    void whenUpdateStudentIfSuccessThenDontThrowAnything() {

        assertDoesNotThrow(() -> studentsService.updateStudent(new StudentDTO(student_1.getId(), FIRST_NAME_1, LAST_NAME_2, group_1.getGroupId())));
    }

    @Test
    @Order(5)
    void afterUpdateStudentIfSuccessThenGetStudentByIdReturnUpdatedStudent() {
        StudentDTO updatedStudent = new StudentDTO(student_1.getId(), FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        studentsService.updateStudent(updatedStudent);
        StudentDTO actualStudent = studentsService.getStudentById(student_1.getId());
        assertEquals(updatedStudent, actualStudent);
    }

    @Test
    @Order(6)
    void whenDeleteStudentByIdIfSuccessThenDontThrowExceptions() {
        assertDoesNotThrow(() -> studentsService.deleteStudentById(student_2.getId()));
    }

    @Test
    @Order(7)
    void afterDeleteStudentByIdIfSearchReturnEntityNotFoundException() {
        studentsService.deleteStudentById(student_1.getId());
        assertThrows(EntityNotFoundException.class, () -> studentsService.getStudentById(student_1.getId()));
    }

    private GroupDTO insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    public StudentDTO insertStudent(String firstName, String lastaName, long groupId) {
        studentsService.addStudent(new StudentDTO(firstName, lastaName, groupId));
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupId);
    }

}
