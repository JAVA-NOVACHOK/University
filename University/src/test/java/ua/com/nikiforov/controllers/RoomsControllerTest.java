package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.services.room.RoomServiceImpl;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomsControllerTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;
    private static final int TEST_ROOM_NUMBER_4 = 15;
    private static final int TEST_ROOM_NUMBER_5 = 16;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;

    private static final long INVALID_ID = 100500;

    private static final String ROOM_ATTR = "room";
    private static final String ROOMS_ATTR = "rooms";
    private static final String ROOM_NUMBER_ATTR = "roomNumber";
    private static final String ROOM_SEAT_ATTR = "seatNumber";
    private static final String ROOM_ID_ATTR = "roomId";
    private static final String ROOM_ID = "id";
    private static final String ID = "id";
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_EDIT_ROOM = "rooms/edit_room_form";

    private static final String URL_ROOMS = "/rooms/";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_3);
    }

    @Test
    void givenRoomsPageURI_ReturnsRoomsViewName_WithRoomsModelAttribute() throws Exception {
        this.mockMvc.perform(get(URL_ROOMS))
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andDo(print())
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void addRoomWithParams_AddsRoom() throws Exception {
        this.mockMvc
                .perform(post("/rooms/add/")
                        .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_4 + STR)
                        .param(ROOM_SEAT_ATTR,
                                TEST_SEAT_NUMBER_1 + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3, roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_4))))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void addRoomWithDuplicateParams_FailAddRoom() throws Exception {
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
    void whenDeleteRoomWithValidId_DeletesRoom() throws Exception {
        this.mockMvc
                .perform(post("/rooms/delete/").param(ROOM_ID, room_1.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_2, room_3)))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void whenDeleteRoomUriWithInvalidId_FailMessage() throws Exception {
        this.mockMvc
                .perform(post("/rooms/delete/")
                        .param(ROOM_ID, INVALID_ID + STR))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ROOMS_ATTR))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void whenEditRoomWithValidRoomId_ReturnsEditForm() throws Exception {
        this.mockMvc
                .perform(
                        get("/rooms/edit/")
                                .param(ROOM_ID_ATTR, room_2.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ROOM_ATTR))
                .andExpect(view().name(VIEW_EDIT_ROOM));
    }

    @Test
    void editRoomWithInvalidRoomId_FailEdit_ReturnsRoomsView() throws Exception {
        this.mockMvc
                .perform(get("/rooms/edit/")
                        .param(ROOM_ID_ATTR, INVALID_ID + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1,room_2, room_3)))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void whenEditRoom_Success_EditsRoom() throws Exception {
        RoomDTO updatedRoom = new RoomDTO(room_2.getId(),TEST_ROOM_NUMBER_5,room_2.getSeatNumber());
        this.mockMvc
                .perform(post("/rooms/edit")
                        .param(ID, room_2.getId() + STR)
                        .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_5 + STR)
                        .param(ROOM_SEAT_ATTR, room_2.getSeatNumber() + STR)
                        .sessionAttr(ROOM_ATTR, new Room()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOM_ATTR, updatedRoom))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }

    @Test
    void editRoomWithDuplicateRoomAttr_FailUpdate_EditsRoom() throws Exception {
        this.mockMvc
                .perform(post("/rooms/edit")
                        .param(ID, room_2.getId() + STR)
                        .param(ROOM_NUMBER_ATTR, TEST_ROOM_NUMBER_3 + STR)
                        .param(ROOM_SEAT_ATTR, TEST_SEAT_NUMBER_1 + STR)
                        .sessionAttr(ROOM_ATTR, new Room()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(view().name(VIEW_ROOMS));
    }


    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        return roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
    }

}
