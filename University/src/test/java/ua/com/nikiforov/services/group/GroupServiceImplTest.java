package ua.com.nikiforov.services.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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

import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.config.DatabaseConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;

@SpringJUnitConfig(DatabaseConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class GroupServiceImplTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

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

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddGroupIfSuccessThenReturnTrue() {
        assertTrue(groupService.addGroup(TEST_GROUP_NAME_1));
    }

    @Test
    void whenGetGroupByIdReturnCorrectGroup() {
        Group group = insertGroup(TEST_GROUP_NAME_1);
        assertEquals(group, groupService.getGroupById(group.getId()));
    }

    @Test
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_1));
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_2));
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_3));
        assertIterableEquals(expectedGroups, groupService.getAllGroups());

    }

    @Test
    void whenUpdateGroupByIdIfSuccessThenReturnTrue() {
        Group group = insertGroup(TEST_GROUP_NAME_1);
        assertTrue(groupService.updateGroup(group.getId(), TEST_GROUP_NAME_2));
    }

    @Test
    void whenUpdateGroupThenGroupHasChangedName() {
        long groupId = insertGroup(TEST_GROUP_NAME_1).getId();
        groupService.updateGroup(groupId, TEST_GROUP_NAME_2);
        Group expectedUpdatedGroup = groupService.getGroupById(groupId);
        Group actualUpdatedGroup = groupService.getGroupByName(TEST_GROUP_NAME_2);
        assertEquals(expectedUpdatedGroup, actualUpdatedGroup);
    }

    @Test
    void whenDeleteGroupByIdIfSuccessThenReturnTrue() {
        Group group = insertGroup(TEST_GROUP_NAME_1);
        assertTrue(groupService.deleteGroup(group.getId()));
    }

    @Test
    void afterDeleteGroupIfSearchReturnEntityNotFoundException() {
        long groupId = insertGroup(TEST_GROUP_NAME_1).getId();
        groupService.deleteGroup(groupId);
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(groupId));
    }

    @Test
     void whenGetStudentsFromGroupByIdReturnListOfStudentsInGroup() {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getId();
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getId();

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1));

        insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2);
        insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2);

        List<Student> actualStudents = groupService.getStudentsByGroupId(groupId_1);
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    void whenDeleteStudentListOfStudentsWithoutStudent() {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getId();
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getId();

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1));
        Student studentToRemove = insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1);

        insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2);
        insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2);

        studentsService.deleteStudentById(studentToRemove.getId());

        List<Student> actualStudents = groupService.getStudentsByGroupId(groupId_1);
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    void whenTransferStudentListOfStudentsInGroupWithTransferedStudent() {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getId();
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getId();

        insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1);
        insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1);

        long transferedStudentId = insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1).getId();

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2));
        expectedStudents.add(insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2));

        studentsService.transferStudent(transferedStudentId, groupId_2);
        
        Student transferedStudent = studentsService.getStudentById(transferedStudentId);
        expectedStudents.add(transferedStudent);
        
        List<Student> actualStudents = groupService.getStudentsByGroupId(groupId_2);
        Collections.sort(actualStudents);
        Collections.sort(expectedStudents);
        assertIterableEquals(expectedStudents, actualStudents);

    }

    public Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    private Student insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(firstName, lastaName, groupName);
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
    }

}
