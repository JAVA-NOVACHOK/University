package ua.com.nikiforov.controllers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.nikiforov.config.WebConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
class GroupsControllerTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    
    private static final long INVALID_GROUP_ID = 100500l;
    
    private static final String URL_GROUPS = "/groups/";
    private static final String URL_ADD = "/groups/add/";
    private static final String URL_DELETE = "/groups/delete/";
    private static final String URL_EDIT = "/groups/edit/";
    
    private static final String GROUP_ATTR = "group";
    private static final String GROUP_ID_ATTR = "groupId";
    private static final String GROUP_NAME_ATTR = "groupName";
    private static final String GROUPS_ATTR = "groups";
    
    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";
    
    private static final String VIEW_GROUPS = "groups/groups";
    private static final String VIEW_EDIT_GROUP = "groups/edit_group_form";
    private static final String VIEW_DELETE_GROUP = "groups/delete_group_form";

    @Autowired
    private GroupService groupService;

    @Autowired
    private TableCreator tableCreator;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenGroupPageURI_whenMockMVC_thenReturnsGroupsViewName() throws Exception {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        Group group_3 = insertGroup(TEST_GROUP_NAME_3);
        this.mockMvc.perform(get(URL_GROUPS))
        .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
        .andDo(print()).andExpect(view().name(VIEW_GROUPS));
    }
    
    @Test
    void givenGroupAddUriWithParams_AddGroup() throws Exception {
        this.mockMvc
                .perform(post(URL_ADD)
                        .param(GROUP_NAME_ATTR,TEST_GROUP_NAME_1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(GROUPS_ATTR))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    } 
    
    
    @Test
    void givenDeleteUtlForGetMapping_ReturnDeleteFormView() throws Exception{
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        Group group_3 = insertGroup(TEST_GROUP_NAME_3);
        this.mockMvc.perform(get(URL_DELETE))
        .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
        .andDo(print()).andExpect(view().name(VIEW_DELETE_GROUP));
    }
    
    @Test
    void givenGroupDeleteUriByValidId_DeletGroup() throws Exception {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        Group group_3 = insertGroup(TEST_GROUP_NAME_3);
        this.mockMvc
                .perform(post(URL_DELETE)
                        .param(GROUP_ID_ATTR,group_1.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(GROUPS_ATTR,hasItems(group_2,group_3)))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_GROUPS));
    } 
    @Test
    void givenGroupDeleteUriByInvalidId_NotDeleteGroup() throws Exception {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        Group group_3 = insertGroup(TEST_GROUP_NAME_3);
        this.mockMvc
        .perform(post(URL_DELETE)
                .param(GROUP_ID_ATTR,INVALID_GROUP_ID + STR))
        .andExpect(status().isOk())
        .andExpect(model().attribute(GROUPS_ATTR,hasItems(group_1,group_2,group_3)))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_GROUPS));
    } 
    
    
    @Test
    void givenGroupEditGetURI_ReturnsGroupEditViewForm() throws Exception {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        this.mockMvc
        .perform(get(URL_EDIT).param(GROUP_ID_ATTR,group_1.getId() + STR))
        .andExpect(model().attribute(GROUP_ATTR, group_1))
        .andDo(print())
        .andExpect(view().name(VIEW_EDIT_GROUP));
    }
    
    @Test
    void givenGroupEditPostURI_EditGroupAndReturnsGroupsView() throws Exception {
        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group updatedGroup = new Group(group_1.getId(), TEST_GROUP_NAME_2);
        this.mockMvc
        .perform(post(URL_EDIT)
                .param(GROUP_ID_ATTR, group_1.getId() + STR)
                .param(GROUP_NAME_ATTR, TEST_GROUP_NAME_2)
                .sessionAttr(GROUP_ATTR, new Group()))
        .andExpect(model().attribute(GROUPS_ATTR,hasItem(updatedGroup)))
        .andDo(print())
        .andExpect(view().name(VIEW_GROUPS));
    }

    private Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }
}
