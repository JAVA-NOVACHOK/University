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
import ua.com.nikiforov.dao.tablecreator.TableCreator;
import ua.com.nikiforov.models.Group;

@SpringJUnitConfig(UniversityConfig.class)
class GroupServiceImplTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    private static final String SPACE = " ";
    private static final String NEW_LINE = System.lineSeparator();

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
    void whenGetGroupByNameReturnCorrectGroup() {
        Group group = insertGroup();
        assertEquals(TEST_GROUP_NAME_1, group.getGroupName());
    }

    @Test
    void whenGetAllGroupsIfPresentReturnListOfAllGroups() {
        groupService.addGroup(TEST_GROUP_NAME_1);
        groupService.addGroup(TEST_GROUP_NAME_2);
        groupService.addGroup(TEST_GROUP_NAME_3);
        StringBuilder expected = new StringBuilder();
        expected.append(TEST_GROUP_NAME_1).append(SPACE).append(TEST_GROUP_NAME_2).append(SPACE)
                .append(TEST_GROUP_NAME_3).append(SPACE);
        StringBuilder actual = new StringBuilder();
        long count = groupService.getAllGroups().stream().map(g -> actual.append(g.getGroupName()).append(SPACE))
                .count();
        assertEquals(expected.toString(), actual.toString());
        assertEquals(GROUP_TEST_COUNT_3, count);
    }

    @Test
    void whenGetGroupByIdReturnCorrectGroup() {
        long groupId = insertGroup().getId();
        assertEquals(groupId, groupService.getGroupById(groupId).getId());
    }

    @Test
    void whenUpdateGroupByIdIfSuccessThenReturnTrue() {
        Group group = insertGroup();
        assertTrue(groupService.updateGroup(group.getId(), TEST_GROUP_NAME_2));
    }

    @Test
    void whenUpdateGroupIfSuccessThenGetGroupByIdAfterUpdateReturnChangedName() {
        long groupId = insertGroup().getId();
        groupService.updateGroup(groupId, TEST_GROUP_NAME_2);
        assertEquals(TEST_GROUP_NAME_2, groupService.getGroupById(groupId).getGroupName());
    }

    @Test
    void whenDeleteGroupByIdIfSuccessThenReturnTrue() {
        Group group = insertGroup();
        assertTrue(groupService.deleteGroup(group.getId()));
    }

    @Test
    void afterDeleteGroupByIdIfSearchForItReturnEmptyResultDataAccessException() {
        long groupId = insertGroup().getId();
        groupService.deleteGroup(groupId);
        assertThrows(EmptyResultDataAccessException.class, () -> groupService.getGroupById(groupId));
    }

    private Group insertGroup() {
        groupService.addGroup(TEST_GROUP_NAME_1);
        return groupService.getGroupByName(TEST_GROUP_NAME_1);
    }

}
