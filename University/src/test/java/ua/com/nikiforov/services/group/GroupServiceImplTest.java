package ua.com.nikiforov.services.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Group;

@SpringJUnitConfig(UniversityConfig.class)
class GroupServiceImplTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    @Autowired
    private GroupService groupService;

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
    void whenUpdateGroupIfSuccessThenGetGroupByIdAfterUpdateReturnChangedName() {
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
    void afterDeleteGroupByIdIfSearchForItReturnEmptyResultDataAccessException() {
        long groupId = insertGroup(TEST_GROUP_NAME_1).getId();
        groupService.deleteGroup(groupId);
        assertThrows(EmptyResultDataAccessException.class, () -> groupService.getGroupById(groupId));
    }

    private Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

}
