package ua.com.nikiforov.rest_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.services.group.GroupService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupsRestControllerTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";
    private static final String TEST_GROUP_NAME_4 = "AA-15";
    private static final String TEST_WRONG_PATTERN_NAME = "A-1";

    private static final long INVALID_ID = 100500;

    private static final String JSON_ROOT = "$";

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
    void whenGetAllGroups_Status200_ReturnRoomList() throws Exception {
        this.mockMvc.perform(get("/api/groups/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].groupName", is(group_1.getGroupName())))
                .andExpect(jsonPath("$[1].groupName", is(group_2.getGroupName())))
                .andExpect(jsonPath("$[2].groupName", is(group_3.getGroupName())));
    }

    @Test
    void whenGetGroupByValidId_Stats200_ReturnGroup() throws Exception {
        this.mockMvc.perform(get("/api/groups/{groupId}", group_1.getGroupId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName", is(group_1.getGroupName())));
    }

    @Test
    void whenAddGroup_Status200_GroupAdd() throws Exception {
        GroupDTO groupDTO = new GroupDTO(TEST_GROUP_NAME_4);
        this.mockMvc.perform(post("/api/groups/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName", is(groupDTO.getGroupName())));
    }

    @Test
    void whenUpdateGroupName_Status200_GroupUpdate() throws Exception {
        GroupDTO groupDTO = insertGroup(TEST_GROUP_NAME_4);
        this.mockMvc.perform(put("/api/groups/{groupId}", groupDTO.getGroupId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName", is(groupDTO.getGroupName())));
    }

    @Test
    void whenDeleteGroup_Status200_GroupDelete() throws Exception {
        this.mockMvc.perform(delete("/api/groups/{groupId}", group_1.getGroupId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddGroupWithWrongPattern_Status400() throws Exception {
        GroupDTO groupDTO = new GroupDTO(TEST_WRONG_PATTERN_NAME);
        List<String> errors = new ArrayList<>();
        errors.add("Group name must have first two capital letters, dash and two numbers!");
        this.mockMvc.perform(post("/api/groups/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", is(errors)));
    }

    @Test
    void whenGetGroupByInValidId_Stats404_ReturnError() throws Exception {

        this.mockMvc.perform(get("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors").value("Couldn't get Group by id " + INVALID_ID));
    }

    @Test
    void whenDeleteGroupWithInvalidId_Status404() throws Exception {

        this.mockMvc.perform(delete("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors").value("Couldn't get Group with id " + INVALID_ID));
    }

    @Test
    void whenUpdateGroupWithInvalidId_Status404() throws Exception {

        GroupDTO groupDTO = insertGroup(TEST_GROUP_NAME_4);
        this.mockMvc.perform(put("/api/groups/{groupId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors").value("Couldn't get Group GroupDTO{groupId=100500, groupName='AA-15', students=[]} when update"));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GroupDTO insertGroup(String groupName) {
        return groupService.addGroup(new GroupDTO(groupName));
    }

}