package ua.com.nikiforov.services.room;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



class RoomServiceImplTest extends SetupTestHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImplTest.class);

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;
    private static final int TEST_ROOM_NUMBER_4 = 15;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;

    @Autowired
    private RoomServiceImpl roomService;

    @BeforeAll
    void setup() {
        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

    }

    @Test
    void whenGetAllRoomsThenReturnGroupList() {
        List<RoomDTO> expectedRooms = new ArrayList<>();
        expectedRooms.add(room_1);
        expectedRooms.add(room_2);
        expectedRooms.add(room_3);

        List<RoomDTO> actualRooms = roomService.getAllRooms();
        assertIterableEquals(expectedRooms, actualRooms);
    }

    @Test
    void afterAddRoomGetRoomByIdReturnCorrectRoom() {
        assertEquals(room_2, roomService.getRoomById(room_2.getId()));
    }


    @Test
    void whenDeleteRoomByIdIfSuccessDoesNotThrow() {
        assertDoesNotThrow(() -> roomService.deleteRoomById(room_2.getId()));
    }

    @Test
    void afterDeleteRoomByIdIfSearchReturnEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> roomService.deleteRoomById(room_2.getId()));
    }

    @Test
    void whenUpdateRoomThenGroupHasUpdatedName() {
        int roomId = room_3.getId();
        RoomDTO updatedRoom = new RoomDTO(roomId, TEST_ROOM_NUMBER_4, TEST_SEAT_NUMBER_2);
        roomService.updateRoom(updatedRoom);
        RoomDTO actualRoom = roomService.getRoomById(roomId);
        assertEquals(updatedRoom, actualRoom);
    }

}
