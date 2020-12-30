package ua.com.nikiforov.services.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.persons.StudentsService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class GroupServiceImplTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    private static final String TEST_GROUP_NAME_4 = "AA-15";
    private static final String TEST_GROUP_NAME_5 = "AA-16";
    private static final String TEST_GROUP_NAME_6 = "AA-17";

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Frank";
    private static final String FIRST_NAME_5 = "Bob";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Bird";
    private static final String LAST_NAME_5 = "Crow";

    private GroupDTO group_1;
    private GroupDTO group_2;
    private GroupDTO group_3;
    private GroupDTO group_4;

    private StudentDTO student_1;
    private StudentDTO student_2;
    private StudentDTO student_3;
    private StudentDTO student_4;

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentsService studentsService;

    @BeforeAll
    void setup() {
        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);
        group_4 = insertGroup(TEST_GROUP_NAME_4);

        student_1 = insertStudent(FIRST_NAME_1, LAST_NAME_1, group_1.getGroupId());
        student_2 = insertStudent(FIRST_NAME_2, LAST_NAME_2, group_1.getGroupId());
        student_3 = insertStudent(FIRST_NAME_3, LAST_NAME_3, group_1.getGroupId());
        student_4 = insertStudent(FIRST_NAME_4, LAST_NAME_4, group_1.getGroupId());

    }

    @Test
    @Order(1)
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        List<GroupDTO> expectedGroups = new ArrayList<>();
        expectedGroups.add(group_1);
        expectedGroups.add(group_2);
        expectedGroups.add(group_3);
        expectedGroups.add(group_4);
        assertIterableEquals(expectedGroups, groupService.getAllGroups());
    }

    @Test
    @Order(2)
    void whenAddGroupIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> groupService.addGroup(TEST_GROUP_NAME_6));
    }

    @Test
    @Order(3)
    void whenGetGroupByIdReturnCorrectGroup() {
        assertEquals(group_1, groupService.getGroupById(group_1.getGroupId()));
    }


    @Test
    @Order(4)
    void whenUpdateGroupByIdIfSuccessThenReturnTrue() {
        GroupDTO expectedGroup = new GroupDTO(group_3.getGroupId(), TEST_GROUP_NAME_5);
        groupService.updateGroup(expectedGroup);
        GroupDTO updatedGroup = groupService.getGroupById(group_3.getGroupId());
        assertEquals(expectedGroup, updatedGroup);
    }


    @Test
    @Order(5)
    void whenDeleteGroupByWithStudentsThrowsDataOperationException() {
        assertThrows(DataOperationException.class, () -> groupService.deleteGroup(group_1.getGroupId()));
    }

    @Test
    @Order(6)
    void afterDeleteGroupIfSearchReturnEntityNotFoundException() {
        groupService.deleteGroup(group_2.getGroupId());
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(group_2.getGroupId()));
    }

    @Test
    @Order(7)
    void whenGetStudentsFromGroupByIdReturnListOfStudentsInGroup() {

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(student_4);
        expectedStudents.add(student_2);
        expectedStudents.add(student_1);
        expectedStudents.add(student_3);
        List<StudentDTO> actualStudents = groupService.getGroupById(group_1.getGroupId()).getStudents();
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    @Order(8)
    void whenDeleteStudentListOfStudents_StudentNotInList() {

        studentsService.deleteStudentById(student_1.getId());

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(student_4);
        expectedStudents.add(student_2);
        expectedStudents.add(student_3);
        List<StudentDTO> actualStudents = groupService.getGroupById(group_1.getGroupId()).getStudents();
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    @Order(9)
    void whenTransferStudentListOfStudentsInGroupWithTransferedStudent() {

        studentsService.transferStudent(student_2.getId(), group_4.getGroupId());

        List<StudentDTO> expectedStudentsGroupTo = new ArrayList<>();
        expectedStudentsGroupTo.add(studentsService.getStudentById(student_2.getId()));

        List<StudentDTO> expectedStudentsGroupFrom = new ArrayList<>();
        expectedStudentsGroupFrom.add(student_4);
        expectedStudentsGroupFrom.add(student_3);

        List<StudentDTO> actualStudentsGroupFrom = groupService.getGroupById(group_1.getGroupId()).getStudents();
        assertIterableEquals(expectedStudentsGroupFrom, actualStudentsGroupFrom);

        List<StudentDTO> actualStudentsGroupTo = groupService.getGroupById(group_4.getGroupId()).getStudents();
        assertIterableEquals(expectedStudentsGroupTo, actualStudentsGroupTo);
    }

    public GroupDTO insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    private StudentDTO insertStudent(String firstName, String lastaName, long groupId) {
        studentsService.addStudent(new StudentDTO(firstName, lastaName, groupId));
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupId);
    }

}
