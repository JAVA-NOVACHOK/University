package ua.com.nikiforov.rest_controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class RoomsRestControllerTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;
    private static final int TEST_ROOM_NUMBER_4 = 15;
    private static final int TEST_ROOM_NUMBER_5 = 16;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;

    private static final long INVALID_ID = 100500;

    private static final String JSON_ROOT = "$";

    private static final String ROOM_ATTR = "room";
    private static final String ROOMS_ATTR = "rooms";
    private static final String ROOM_NUMBER_ATTR = "roomNumber";
    private static final String ROOM_SEAT_ATTR = "seatNumber";
    private static final String ROOM_ID_ATTR = "roomId";
    private static final String ROOM_ID = "id";
    private static final String ID = "id";
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_EDIT_ROOM = "rooms/edit_room_form";

    private static final String REST_URL_ROOMS = "/rest_rooms";

    private static final String URL_ADD = "/rooms/add/";
    private static final String URL_DELETE = "/rooms/delete/";
    private static final String URL_EDIT = "/rooms/edit/";

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

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_3);
    }

    @Test
    @Order(1)
    void givenRoomsPageURI_ReturnsRoomsViewName_WithRoomsModelAttribute() throws Exception {
        this.mockMvc.perform(get(REST_URL_ROOMS).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].roomNumber", is(room_1.getRoomNumber())))
                .andExpect(jsonPath("$[1].roomNumber", is(room_2.getRoomNumber())))
                .andExpect(jsonPath("$[2].roomNumber", is(room_3.getRoomNumber())));
    }

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        return roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
    }

}