package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.services.group.GroupService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class GroupsControllerTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    private static final String TEST_GROUP_NAME_4 = "AA-15";
    private static final String TEST_GROUP_NAME_5 = "AA-16";
    private static final String TEST_GROUP_NAME_6 = "AB-16";
    private static final String TEST_EMPTY_NAME = "";

    private static final long INVALID_GROUP_ID = 100500;

    private static final String URL_GROUPS = "/groups/";
    private static final String URL_ADD = "/groups/add/";
    private static final String URL_DELETE = "/groups/delete/";
    private static final String URL_EDIT = "/groups/edit/";

    private static final String GROUP_ATTR = "group";
    private static final String GROUP_ID_ATTR = "groupId";
    private static final String GROUP_NAME_ATTR = "groupName";
    private static final String GROUPS_ATTR = "groups";
    private static final String ERROR_ATTR = "errorMessages";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String ERROR_MSG = "1. ERROR! Group name must have first two capital letters, dash and two numbers!";
    private static final String STR = "";

    private static final String VIEW_GROUPS = "groups/groups";
    private static final String VIEW_EDIT_GROUP = "groups/edit_group_form";
    private static final String VIEW_DELETE_GROUP = "groups/delete_group_form";
    private static final String VIEW_ERROR = "errors/error";

    @Autowired
    private GroupService groupService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private GroupDTO group_1;
    private GroupDTO group_2;
    private GroupDTO group_3;


    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);

    }

    @Test
    @Order(1)
    void givenGroupPageURI_whenMockMVC_thenReturnsGroupsViewName() throws Exception {

        this.mockMvc.perform(get(URL_GROUPS))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andDo(print()).andExpect(view().name(VIEW_GROUPS));
    }

    @Test
    @Order(2)
    void whenAddGroupWithParams_GroupAddsSuccessfully() throws Exception {
        this.mockMvc
                .perform(post("/groups/add/")
                        .param(GROUP_NAME_ATTR, TEST_GROUP_NAME_4))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(GROUPS_ATTR))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    }

    @Test
    void whenAddGroupWithEmptyName_ErrorHandles() throws Exception {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ERROR_MSG);
        this.mockMvc
                .perform(post("/groups/add/")
                        .param(GROUP_NAME_ATTR, TEST_EMPTY_NAME))
                .andExpect(model().attribute(ERROR_ATTR, errorMessages))
                .andExpect(view().name(VIEW_ERROR));
    }

    @Test
    @Order(3)
    void addGroupWithDuplicateParams_FailMSG() throws Exception {
        this.mockMvc
                .perform(post("/groups/add/")
                        .param(GROUP_NAME_ATTR, TEST_GROUP_NAME_1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(GROUPS_ATTR))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    }


    @Test
    @Order(4)
    void givenDeleteUtlForGetMapping_ReturnDeleteFormView() throws Exception {
        this.mockMvc.perform(
                get("/groups/delete/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andDo(print())
                .andExpect(view().name(VIEW_DELETE_GROUP));
    }

    @Test
    @Order(5)
    void deleteGroupByValidId_SuccessDeletGroup() throws Exception {
        this.mockMvc
                .perform(post("/groups/delete/")
                        .param(GROUP_ID_ATTR, group_1.getGroupId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_2, group_3)))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    }

    @Test
    @Order(6)
    void deleteGroupByInvalidId_FailDeleteGroup() throws Exception {
        this.mockMvc
                .perform(post("/groups/delete/")
                        .param(GROUP_ID_ATTR, INVALID_GROUP_ID + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_2, group_3)))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    }


    @Test
    @Order(7)
    void editGroupByValidId_ReturnsSuccesss_GroupEditViewForm() throws Exception {
        this.mockMvc
                .perform(get("/groups/edit/").param(GROUP_ID_ATTR, group_2.getGroupId() + STR))
                .andExpect(model().attribute(GROUP_ATTR, group_2))
                .andDo(print())
                .andExpect(view().name(VIEW_EDIT_GROUP));
    }

    @Test
    @Order(8)
    void editGroupByInvalidId_FailEdit_ReturnsGroupsList() throws Exception {
        this.mockMvc
                .perform(get("/groups/edit/")
                        .param(GROUP_ID_ATTR, INVALID_GROUP_ID + STR))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_2, group_3)))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andDo(print())
                .andExpect(view().name(VIEW_GROUPS));
    }

    @Test
    @Order(9)
    void givenGroupEditPostURI_EditGroupAndReturnsGroupsView() throws Exception {
        GroupDTO updatedGroup = new GroupDTO(group_2.getGroupId(), TEST_GROUP_NAME_6);
        this.mockMvc
                .perform(post("/groups/edit/")
                        .param(GROUP_ID_ATTR, group_2.getGroupId() + STR)
                        .param(GROUP_NAME_ATTR, TEST_GROUP_NAME_6)
                        .sessionAttr(GROUP_ATTR, new Group()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItem(updatedGroup)))
                .andDo(print())
                .andExpect(view().name(VIEW_GROUPS));
    }

    private GroupDTO insertGroup(String groupName) {
        groupService.addGroup(new GroupDTO(groupName));
        return groupService.getGroupByName(groupName);
    }
}
