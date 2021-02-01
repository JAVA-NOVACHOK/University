package ua.com.nikiforov.rest_controllers.db_rider_test;

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
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoomsRestControllerTestDBRider extends SetupTestHelper {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final int TEST_ROOM_NUMBER_1 = 1;
    private static final int TEST_ROOM_NUMBER_2 = 2;
    private static final int TEST_ROOM_NUMBER_3 = 3;
    private static final int TEST_ROOM_NUMBER_4 = 4;
    private static final int TEST_ROOM_NUMBER_5 = 5;

    private static final int TEST_SEAT_NUMBER_1 = 10;
    private static final int TEST_SEAT_NUMBER_2 = 15;
    private static final int TEST_SEAT_NUMBER_3 = 20;

    private static final int ID_1 = 1;



    private static final int INVALID_ID = 100500;

    @BeforeAll
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(value = ADD_THREE_ROOMS_XML, cleanAfter = true)
    @ExpectedDataSet(value = ADD_THREE_ROOMS_XML, ignoreCols = "room_id")
    void whenGetAllGroups_Status200_ReturnsRoomsSize() throws Exception {
        this.mockMvc.perform(get("/api/rooms/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DataSet(value = ADD_ONE_ROOM_XML, executeScriptsBefore = RESET_ROOM_ID, cleanAfter = true)
    @ExpectedDataSet(value = ADD_ONE_ROOM_ID_XML)
    void whenGetRoomById_Status200_ReturnRoom() throws Exception {
        this.mockMvc.perform(get("/api/rooms/{roomId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(TEST_ROOM_NUMBER_1))
                .andExpect(jsonPath("$.seatNumber").value(TEST_SEAT_NUMBER_1));
    }

    @Test
    @DataSet(value = ADD_ONE_ROOM_XML, cleanAfter = true)
    void whenGetRoomWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(get("/api/rooms/{roomId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("errors").value(String.format("Couldn't get Room by id %d", INVALID_ID)));
    }

    @Test
    @DataSet(cleanAfter = true)
    void whenAddRoom_Status200_ReturnAddedRoom() throws Exception {
        RoomDTO newRoom = new RoomDTO(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        this.mockMvc.perform(post("/api/rooms/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newRoom)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(TEST_ROOM_NUMBER_1))
                .andExpect(jsonPath("$.seatNumber").value(TEST_SEAT_NUMBER_1));
    }

    @Test
    @DataSet(value = ADD_THREE_ROOMS_XML, cleanAfter = true)
    void whenAddRoomWithDuplicateName_Status400() throws Exception {
        RoomDTO newDuplicateRoom = new RoomDTO(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        this.mockMvc.perform(post("/api/rooms/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newDuplicateRoom)))
                .andExpect(jsonPath(STATUS).value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath(ERRORS).value(String.format("ERROR! Already exists Room with number %d", newDuplicateRoom.getRoomNumber())));

    }

    @Test
    @DataSet(value = ADD_ONE_ROOM_XML, executeScriptsBefore = RESET_ROOM_ID, cleanAfter = true)
    void whenUpdateRoom_Status200_RoomUpdates() throws Exception {
        RoomDTO updatedRoom = new RoomDTO(ID_1, TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2);
        this.mockMvc.perform(put("/api/rooms/{roomId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRoom)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(TEST_ROOM_NUMBER_2))
                .andExpect(jsonPath("$.seatNumber").value(TEST_SEAT_NUMBER_2));
    }

    @Test
    @DataSet(value = ADD_THREE_ROOMS_XML, executeScriptsBefore = RESET_ROOM_ID, cleanAfter = true)
    void whenUpdateRoomWithDuplicateName_Status400() throws Exception {
        RoomDTO updatedRoom = new RoomDTO(ID_1, TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);
        this.mockMvc.perform(put("/api/rooms/{roomId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRoom)))
                .andExpect(jsonPath(ERRORS).value(String.format("ERROR! Already exists Room with number %d", updatedRoom.getRoomNumber())))
                .andExpect(jsonPath(STATUS).value(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    @DataSet(value = ADD_ONE_ROOM_XML, executeScriptsBefore = RESET_ROOM_ID, cleanAfter = true)
    void whenDeleteRoom_Status200_RoomDeletes() throws Exception {
        this.mockMvc.perform(delete("/api/rooms/{roomId}", ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/rooms/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    @DataSet(value = ADD_ONE_ROOM_XML, executeScriptsBefore = RESET_ROOM_ID, cleanAfter = true)
    void whenDeleteRoomWithInvalidId_Status404() throws Exception {
        this.mockMvc.perform(delete("/api/rooms/{roomId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS).value(String.format("Couldn't delete Room by ID %d", INVALID_ID)));

    }

}
