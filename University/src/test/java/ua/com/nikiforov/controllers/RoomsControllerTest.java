package ua.com.nikiforov.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
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
import ua.com.nikiforov.controllers.model_atributes.ScheduleFindAttr;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.services.room.RoomServiceImpl;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
class RoomsControllerTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;
    
    private static final long INVALID_ID = 100500;
    
    private static final String ROOM_ATTR = "room";
    private static final String ROOMS_ATTR = "rooms";
    private static final String ROOM_NUMBER_ATTR = "roomNumber";
    private static final String ROOM_SEAT_ATTR = "seatNumber";
    private static final String ROOM_ID_ATTR = "roomId";
    private static final String ID = "id";
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_EDIT_ROOM = "rooms/edit_room_form";
    
    private static final String URL_ROOMS = "/rooms/";
    private static final String URL_ADD = "/rooms/add/";
    private static final String URL_DELETE = "/rooms/delete/";
    private static final String URL_EDIT = "/rooms/edit/";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private TableCreator tableCreator;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    public void init() {
        tableCreator.createTables();
    }

    @Test
    void givenRoomsPageURI_ReturnsRoomsViewName_WithRoomsModelAttribute() throws Exception {
        Room room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        Room room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2);
        Room room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_3);
        this.mockMvc.perform(get(URL_ROOMS)).andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andDo(print()).andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void addRoomWithParams_AddsRoom() throws Exception {
        this.mockMvc
                .perform(post("/rooms/add/")
                        .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_1 + STR)
                        .param(ROOM_SEAT_ATTR,
                        TEST_SEAT_NUMBER_1 + STR))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ROOMS_ATTR))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }
    
    @Test
    void addRoomWithDuplicateParams_FailAddRoom() throws Exception {
        insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        
        this.mockMvc
        .perform(post("/rooms/add/")
                .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_1 + STR)
                .param(ROOM_SEAT_ATTR,
                        TEST_SEAT_NUMBER_1 + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(ROOMS_ATTR))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void givenRoomDeleteUriWithValidId_DeletesRoom() throws Exception {
        Room room = insertRoom(TEST_SEAT_NUMBER_1, TEST_SEAT_NUMBER_2);
        this.mockMvc
        .perform(get("/rooms/delete/").param(ROOM_ID_ATTR, room.getId() + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(ROOMS_ATTR))
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }
    @Test
    void givenRoomDeleteUriWithInvalidId_DeletesRoom() throws Exception {
        insertRoom(TEST_SEAT_NUMBER_1, TEST_SEAT_NUMBER_2);
        
        this.mockMvc
        .perform(get("/rooms/delete/")
                .param(ROOM_ID_ATTR,INVALID_ID + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(ROOMS_ATTR))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }
    
    @Test
    void editRoomWithValidRoomId_ReturnsEditForm() throws Exception {
        Room room = insertRoom(TEST_SEAT_NUMBER_1, TEST_SEAT_NUMBER_2);
        this.mockMvc
        .perform(get("/rooms/edit/").param(ROOM_ID_ATTR, room.getId() + STR))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(ROOM_ATTR))
        .andExpect(view().name(VIEW_EDIT_ROOM));
    }
    
    @Test
    void editRoomWithInValidRoomId_FailEdit_ReturnsRoomsView() throws Exception {
        Room room_1 = insertRoom(TEST_SEAT_NUMBER_1, TEST_SEAT_NUMBER_2);
        Room room_2 = insertRoom(TEST_SEAT_NUMBER_2, TEST_SEAT_NUMBER_1);
        Room room_3 = insertRoom(TEST_SEAT_NUMBER_3, TEST_SEAT_NUMBER_3);
        this.mockMvc
        .perform(get("/rooms/edit/")
                .param(ROOM_ID_ATTR, INVALID_ID + STR))
        .andExpect(status().isOk())
        .andExpect(model().attribute(ROOMS_ATTR,hasItems(room_1,room_2,room_3)))
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }
    
    @Test
    void givenRoomEditPostUriWithRoomAttr_EditsRoom() throws Exception{
        Room room = insertRoom(TEST_SEAT_NUMBER_1, TEST_SEAT_NUMBER_1);
        this.mockMvc
        .perform(post("/rooms/edit")
                .param(ID, room.getId() + STR)
                .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_2 + STR)
                .param(ROOM_SEAT_ATTR, room.getSeatNumber() + STR)
                .sessionAttr(ROOM_ATTR, new Room()))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(SUCCESS_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }
    
    @Test
    void editRoomWithDuplicateRoomAttr_FailUpdate_EditsRoom() throws Exception{
        Room room = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        
        this.mockMvc
        .perform(post("/rooms/edit")
                .param(ID, room.getId() + STR)
                .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_2 + STR)
                .param(ROOM_SEAT_ATTR, TEST_SEAT_NUMBER_1 + STR)
                .sessionAttr(ROOM_ATTR, new Room()))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists(FAIL_MSG))
        .andExpect(view().name(VIEW_ROOMS));
    }
    

    private Room insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(roomNumber, seatNumber);
        return roomService.getRoomByRoomNumber(roomNumber);
    }

}
