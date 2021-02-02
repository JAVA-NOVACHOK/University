package ua.com.nikiforov.rest_controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupsRestControllerTest extends SetupTestHelper {

    private static final String GROUP_NAME = "$.groupName";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(value = ADD_THREE_GROUPS_XML, cleanAfter = true)
    void whenGetAllGroups_Status200_ReturnRoomList() throws Exception {
        this.mockMvc.perform(get("/api/groups/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].groupName", is(TEST_GROUP_NAME_1)))
                .andExpect(jsonPath("$[1].groupName", is(TEST_GROUP_NAME_2)))
                .andExpect(jsonPath("$[2].groupName", is(TEST_GROUP_NAME_3)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_GROUP_XML, ignoreCols = "group_id")
    void whenGetGroupByValidId_Stats200_ReturnGroup() throws Exception {
        this.mockMvc.perform(get("/api/groups/{groupId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(GROUP_NAME, is(TEST_GROUP_NAME_1)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    void whenGetGroupByInValidId_Stats404_ReturnError() throws Exception {
        this.mockMvc.perform(get("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS).value("Couldn't get Group by id " + INVALID_ID));
    }

    @Test
    @DataSet(cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_GROUP_XML, ignoreCols = "group_id")
    void whenAddGroup_Status200_GroupAdd() throws Exception {
        GroupDTO groupDTO = new GroupDTO(TEST_GROUP_NAME_1);
        this.mockMvc.perform(post("/api/groups/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(GROUP_NAME, is(TEST_GROUP_NAME_1)));
    }

    @Test
    @DataSet(cleanAfter = true)
    void whenAddGroupWithInvalidPatternName_Status400_ErrorMessage() throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Group name must have first two capital letters, dash and two numbers!");
        GroupDTO groupDTO = new GroupDTO(TEST_WRONG_PATTERN_NAME);
        this.mockMvc.perform(post("/api/groups/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(errors)));
    }
    @Test
    @DataSet(value = ADD_THREE_GROUPS_XML,cleanAfter = true)
    void whenAddGroupWithDuplicateName_Status400_ErrorMessage() throws Exception {
        String errors = String.format("Error! Already exists Group with name %s", TEST_GROUP_NAME_1);
        GroupDTO groupDTO = new GroupDTO(TEST_GROUP_NAME_1);
        this.mockMvc.perform(post("/api/groups/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO)))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(errors)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    void whenUpdateGroupName_Status200_GroupUpdate() throws Exception {
        GroupDTO groupDTO = new GroupDTO(ID_1, TEST_GROUP_NAME_4);
        this.mockMvc.perform(put("/api/groups/{groupId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(GROUP_NAME, is(TEST_GROUP_NAME_4)));
    }

    @Test
    @DataSet(value = ADD_THREE_GROUPS_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    void whenUpdateGroupWithDuplicateName_Status400() throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Group name must have first two capital letters, dash and two numbers!");
        GroupDTO groupDTO = new GroupDTO(ID_1, TEST_GROUP_NAME_2);
        this.mockMvc.perform(put("/api/groups/{groupId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(STATUS, is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(ERRORS, is(errors)));
        this.mockMvc.perform(get("/api/groups/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath(JSON_ROOT, is(TEST_GROUP_NAME_2)));

    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    void whenUpdateGroupWithInvalidId_Status404() throws Exception {
        GroupDTO groupDTO = new GroupDTO(ID_1, TEST_GROUP_NAME_4);
        this.mockMvc.perform(put("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS).value(String.format("Couldn't get Group {groupId=%d, groupName='%s', students=[]} when update",
                        INVALID_ID, groupDTO.getGroupName())));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, executeScriptsBefore = RESET_GROUP_ID, cleanAfter = true)
    void whenDeleteGroup_Status200_GroupDelete() throws Exception {
        this.mockMvc.perform(delete("/api/groups/{groupId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/groups/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(0)));
    }

    @Test
    @DataSet(value = ADD_ONE_GROUP_XML, cleanAfter = true)
    void whenDeleteGroupWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS).value("Couldn't get Group with id " + INVALID_ID));
    }

}
