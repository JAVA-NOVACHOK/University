package ua.com.nikiforov.services.group;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.exceptions.StudentsInGroupException;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.persons.StudentsService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class GroupServiceImplTest extends SetupTestHelper {

    private static final String TEST_GROUP_NAME_5 = "AA-16";
    private static final String TEST_GROUP_NAME_6 = "AA-17";

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Frank";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Bird";

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

    @BeforeEach
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
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        List<GroupDTO> expectedGroups = new ArrayList<>();
        expectedGroups.add(group_1);
        expectedGroups.add(group_2);
        expectedGroups.add(group_3);
        expectedGroups.add(group_4);
        assertIterableEquals(expectedGroups, groupService.getAllGroups());
    }

    @Test
    void whenAddGroupIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> groupService.addGroup(new GroupDTO(TEST_GROUP_NAME_6)));
    }

    @Test
    void whenGetGroupByIdReturnCorrectGroup() {
        assertEquals(group_1, groupService.getGroupById(group_1.getGroupId()));
    }


    @Test
    void whenUpdateGroupByIdIfSuccessThenReturnTrue() {
        GroupDTO expectedGroup = new GroupDTO(group_3.getGroupId(), TEST_GROUP_NAME_5);
        groupService.updateGroup(expectedGroup);
        GroupDTO updatedGroup = groupService.getGroupById(group_3.getGroupId());
        assertEquals(expectedGroup, updatedGroup);
    }

    @Test
    void whenDeleteGroupWithStudents_ThrowsStudentsImGroupException(){
        assertThrows(StudentsInGroupException.class,() -> groupService.deleteGroup(group_1.getGroupId()));
    }



    @Test
    void afterDeleteGroupIfSearchReturnEntityNotFoundException() {
        groupService.deleteGroup(group_2.getGroupId());
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(group_2.getGroupId()));
    }

    @Test
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
    void whenTransferStudent_SuccessTransferStudent() {

        studentsService.transferStudent(student_2.getId(), group_4.getGroupId());

        List<StudentDTO> expectedStudentsGroupTo = new ArrayList<>();
        expectedStudentsGroupTo.add(studentsService.getStudentById(student_2.getId()));

        List<StudentDTO> expectedStudentsGroupFrom = new ArrayList<>();
        expectedStudentsGroupFrom.add(student_4);
        expectedStudentsGroupFrom.add(student_1);
        expectedStudentsGroupFrom.add(student_3);

        List<StudentDTO> actualStudentsGroupFrom = groupService.getGroupById(group_1.getGroupId()).getStudents();
        assertIterableEquals(expectedStudentsGroupFrom, actualStudentsGroupFrom);

        List<StudentDTO> actualStudentsGroupTo = groupService.getGroupById(group_4.getGroupId()).getStudents();
        assertIterableEquals(expectedStudentsGroupTo, actualStudentsGroupTo);
    }

}
