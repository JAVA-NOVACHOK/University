package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.helper.SetupTestHelper;
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


class GroupsControllerTest extends SetupTestHelper {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    private static final String TEST_GROUP_NAME_4 = "AA-15";
    private static final String TEST_GROUP_NAME_5 = "AA-16";
    private static final String TEST_GROUP_NAME_6 = "AB-16";
    private static final String TEST_EMPTY_NAME = "";

    private static final long INVALID_GROUP_ID = 100500;

    private static final String URL_GROUPS = "/groups/";

    private static final String GROUP_ATTR = "group";
    private static final String GROUP_DTO_ATTR = "groupDTO";
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


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);
    }

    @Test
    void givenGroupPageURI_whenMockMVC_thenReturnsGroupsViewName() throws Exception {

        this.mockMvc.perform(get(URL_GROUPS))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andDo(print()).andExpect(view().name(VIEW_GROUPS));
    }

    @Test
    void whenAddGroupWithParams_GroupAddsSuccessfully() throws Exception {
        GroupDTO groupDTO = new GroupDTO(TEST_GROUP_NAME_4);
        this.mockMvc
                .perform(post("/groups/add/")
                        .param(GROUP_ATTR, TEST_GROUP_NAME_4)
                .sessionAttr(GROUP_DTO_ATTR, new GroupDTO()))
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
    void givenDeleteUtlForGetMapping_ReturnDeleteFormView() throws Exception {
        this.mockMvc.perform(
                get("/groups/delete/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andDo(print())
                .andExpect(view().name(VIEW_DELETE_GROUP));
    }

    @Test
    void deleteGroupByValidId_SuccessDeleteGroup() throws Exception {
        this.mockMvc
                .perform(post("/groups/delete/")
                        .param(GROUP_ID_ATTR, group_1.getGroupId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_2, group_3)))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    }

    @Test
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
    void editGroupByValidId_ReturnsSuccess_GroupEditViewForm() throws Exception {
        this.mockMvc
                .perform(get("/groups/edit/").param(GROUP_ID_ATTR, group_2.getGroupId() + STR))
                .andExpect(model().attribute(GROUP_ATTR, group_2))
                .andDo(print())
                .andExpect(view().name(VIEW_EDIT_GROUP));
    }

    @Test
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
}
