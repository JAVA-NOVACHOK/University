package ua.com.nikiforov.rest_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomServiceImpl;

import static javax.management.Query.value;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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


    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;
    private RoomDTO room_4;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_3);
    }

    @Test
    @Order(1)
    void whenGetAllGroups_Status200_ReturnsRoomsSize_RoomsNames() throws Exception {
        this.mockMvc.perform(get("/api/rooms/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(3)))
                .andExpect(jsonPath("$[0].roomNumber", is(room_1.getRoomNumber())))
                .andExpect(jsonPath("$[1].roomNumber", is(room_2.getRoomNumber())))
                .andExpect(jsonPath("$[2].roomNumber", is(room_3.getRoomNumber())));
    }

    @Test
    @Order(2)
    void whenGetRoomById_Status200_ReturnRoom() throws Exception {
        this.mockMvc.perform(get("/api/rooms/{roomId}", room_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(room_1.getRoomNumber()))
                .andExpect(jsonPath("$.seatNumber").value(room_1.getSeatNumber()));
    }

    @Test
    @Order(3)
    void whenAddRoom_Status200_ReturnAddedRoom() throws Exception {
        room_4 = insertRoom(TEST_ROOM_NUMBER_4, TEST_SEAT_NUMBER_1);
        this.mockMvc.perform(post("/api/rooms/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(room_4)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(TEST_ROOM_NUMBER_4))
                .andExpect(jsonPath("$.seatNumber").value(TEST_SEAT_NUMBER_1));
    }


    //Executes only with and after Test Order(3) whenAddRoom_Status200_ReturnAddedRoom() otherwise NPE
    @Test
    @Order(4)
    void whenUpdateRoom_Status200_RoomUpdatesRoomAndSeatNumber() throws Exception {
        room_4.setRoomNumber(TEST_ROOM_NUMBER_5);
        this.mockMvc.perform(put("/api/rooms/{roomId}", room_4.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(room_4)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(TEST_ROOM_NUMBER_5))
                .andExpect(jsonPath("$.seatNumber").value(TEST_SEAT_NUMBER_1));
    }

    @Test
    @Order(5)
    void whenDeleteRoom_Status200_RoomDeletes() throws Exception {
        this.mockMvc.perform(delete("/api/rooms/{roomId}", room_4.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void whenDeleteRoomWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/rooms/{roomId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        return roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
    }

}