package ua.com.nikiforov.services.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String SPACE = " ";

    private static final int GROUP_TEST_COUNT_3 = 3;

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
    void whenAddGroupAndThenGetGroupByNameReturnCorrectGroup() {
        Group group = insertGroup(TEST_GROUP_NAME_1);
        assertEquals(TEST_GROUP_NAME_1, group.getGroupName());
        assertEquals(group.getId(), groupService.getGroupById(group.getId()).getId());
    }
    
    @Test
    void whenGetGroupByIdReturnCorrectGroup() {
        Group group = insertGroup(TEST_GROUP_NAME_1);
        long groupId = group.getId();
        assertEquals(groupId, groupService.getGroupById(groupId).getId());
        assertEquals(group.getGroupName(), groupService.getGroupById(groupId).getGroupName());
    }

    @Test
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        groupService.addGroup(TEST_GROUP_NAME_1);
        groupService.addGroup(TEST_GROUP_NAME_2);
        groupService.addGroup(TEST_GROUP_NAME_3);
        StringBuilder expectedGroupNames = new StringBuilder();
        expectedGroupNames.append(TEST_GROUP_NAME_1).append(SPACE).append(TEST_GROUP_NAME_2).append(SPACE)
                .append(TEST_GROUP_NAME_3).append(SPACE);
        StringBuilder actualGroupNames = new StringBuilder();
        long countGroups = groupService.getAllGroups().stream().map(g -> actualGroupNames.append(g.getGroupName()).append(SPACE))
                .count();
        assertEquals(expectedGroupNames.toString(), actualGroupNames.toString());
        assertEquals(GROUP_TEST_COUNT_3, countGroups);
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
        Group updatedGroup = groupService.getGroupById(groupId);
        assertEquals(TEST_GROUP_NAME_2, updatedGroup.getGroupName());
        assertEquals(groupId, updatedGroup.getId());
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
