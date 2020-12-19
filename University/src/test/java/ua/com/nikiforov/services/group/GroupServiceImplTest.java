package ua.com.nikiforov.services.group;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.persons.StudentsService;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestDataSource.class)
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
        assertDoesNotThrow(() -> groupService.addGroup(TEST_GROUP_NAME_1));
    }

    @Test
    void whenGetGroupByIdReturnCorrectGroup() {
        GroupDTO group = insertGroup(TEST_GROUP_NAME_1);
        assertEquals(group, groupService.getGroupById(group.getGroupId()));
    }

    @Test
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        List<GroupDTO> expectedGroups = new ArrayList<>();
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_1));
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_2));
        expectedGroups.add(insertGroup(TEST_GROUP_NAME_3));
        assertIterableEquals(expectedGroups, groupService.getAllGroups());

    }

    @Test
    void whenUpdateGroupByIdIfSuccessThenReturnTrue() {
        GroupDTO group = insertGroup(TEST_GROUP_NAME_1);
        assertDoesNotThrow(() -> groupService.updateGroup(new GroupDTO(group.getGroupId(), TEST_GROUP_NAME_2)));
    }

    @Test
    void whenUpdateGroupThenGroupHasChangedName() {
        GroupDTO group = insertGroup(TEST_GROUP_NAME_1);
        long groupId = group.getGroupId();
        groupService.updateGroup(new GroupDTO(group.getGroupId(), TEST_GROUP_NAME_2));
        GroupDTO expectedUpdatedGroup = groupService.getGroupById(groupId);
        GroupDTO actualUpdatedGroup = groupService.getGroupByName(TEST_GROUP_NAME_2);
        assertEquals(expectedUpdatedGroup, actualUpdatedGroup);
    }

    @Test
    void whenDeleteGroupByIdIfSuccessThenReturnTrue() {
        GroupDTO group = insertGroup(TEST_GROUP_NAME_1);
        assertDoesNotThrow(() -> groupService.deleteGroup(group.getGroupId()));
    }

    @Test
    void afterDeleteGroupIfSearchReturnEntityNotFoundException() {
        long groupId = insertGroup(TEST_GROUP_NAME_1).getGroupId();
        groupService.deleteGroup(groupId);
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(groupId));
    }

    @Test
     void whenGetStudentsFromGroupByIdReturnListOfStudentsInGroup() {
        GroupDTO group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getGroupId();
        GroupDTO group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getGroupId();

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1));

        insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2);
        insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2);

        List<StudentDTO> actualStudents = group_1.getStudents();
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    void whenDeleteStudentListOfStudentsWithoutStudent() {
        GroupDTO group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getGroupId();
        GroupDTO group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getGroupId();

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1));
        expectedStudents.add(insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1));
        
        StudentDTO studentToRemove = insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1);

        insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2);
        insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2);

        studentsService.deleteStudentById(studentToRemove.getId());
        List<StudentDTO> actualStudents = group_1.getStudents();
        assertIterableEquals(expectedStudents, actualStudents);

    }

    @Test
    void whenTransferStudentListOfStudentsInGroupWithTransferedStudent() {
        GroupDTO group_1 = insertGroup(TEST_GROUP_NAME_1);
        long groupId_1 = group_1.getGroupId();
        GroupDTO group_2 = insertGroup(TEST_GROUP_NAME_2);
        long groupId_2 = group_2.getGroupId();

        insertStudent(FIRST_NAME_1, LAST_NAME_1, groupId_1);
        insertStudent(FIRST_NAME_2, LAST_NAME_2, groupId_1);

        long transferedStudentId = insertStudent(FIRST_NAME_3, LAST_NAME_3, groupId_1).getGroupId();

        List<StudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.add(insertStudent(FIRST_NAME_4, LAST_NAME_4, groupId_2));
        expectedStudents.add(insertStudent(FIRST_NAME_5, LAST_NAME_5, groupId_2));

        studentsService.transferStudent(transferedStudentId, groupId_2);
        
        StudentDTO transferedStudent = studentsService.getStudentById(transferedStudentId);
        expectedStudents.add(transferedStudent);
        
        List<StudentDTO> actualStudents = group_2.getStudents();
        assertIterableEquals(expectedStudents, actualStudents);

    }

    public GroupDTO insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    private StudentDTO insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(new StudentDTO(firstName, lastaName, groupName));
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
    }

}
