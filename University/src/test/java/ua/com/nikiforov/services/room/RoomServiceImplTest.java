package ua.com.nikiforov.services.room;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.EntityNotFoundException;


import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class RoomServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImplTest.class);

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;
    private static final int TEST_ROOM_NUMBER_4 = 15;
    private static final int TEST_ROOM_NUMBER_5 = 16;
    private static final int TEST_ROOM_NUMBER_6 = 17;
    private static final int TEST_ROOM_NUMBER_7 = 18;
    private static final int TEST_ROOM_NUMBER_8 = 19;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;
    private RoomDTO room_4;
    private RoomDTO room_5;
    private RoomDTO room_6;
    private RoomDTO room_7;

    @Autowired
    private RoomServiceImpl roomService;

    @BeforeAll
    void setup() {
        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);
        room_4 = insertRoom(TEST_ROOM_NUMBER_4, TEST_SEAT_NUMBER_1);
        room_5 = insertRoom(TEST_ROOM_NUMBER_5, TEST_SEAT_NUMBER_1);
        room_6 = insertRoom(TEST_ROOM_NUMBER_6, TEST_SEAT_NUMBER_1);
        room_7 = insertRoom(TEST_ROOM_NUMBER_7, TEST_SEAT_NUMBER_1);
    }

    @Test
    @Order(1)
    void whenGetAllRoomsThenReturnGroupList() {
        List<RoomDTO> expectedRooms = new ArrayList<>();
        expectedRooms.add(room_1);
        expectedRooms.add(room_2);
        expectedRooms.add(room_3);
        expectedRooms.add(room_4);
        expectedRooms.add(room_5);
        expectedRooms.add(room_6);
        expectedRooms.add(room_7);

        List<RoomDTO> actualRooms = roomService.getAllRooms();
        assertIterableEquals(expectedRooms, actualRooms);
    }

    @Test
    void afterAddRoomGetRoomByIdReturnCorrectRoom() {
        assertEquals(room_2, roomService.getRoomById(room_2.getId()));
    }


    @Test
    void whenDeleteRoomByIdIfSuccessThenReturnTrue() {
        int room_2Id = room_2.getId();
        assertDoesNotThrow(() -> roomService.deleteRoomById(room_2Id));
    }

    @Test
    void afterDeleteRoomByIdIfSearchReturnEntityNotFoundException() {
        int room_1Id = room_1.getId();
        roomService.deleteRoomById(room_1Id);
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoomById(room_1Id));
    }

    @Test
    void whenUpdateRoomThenGroupHasUpdatedName() {
        int roomId = room_7.getId();
        roomService.updateRoom(new RoomDTO(roomId, TEST_ROOM_NUMBER_8, TEST_SEAT_NUMBER_2));
        RoomDTO expectedRoom = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_8);
        RoomDTO actualRoom = roomService.getRoomById(roomId);
        assertEquals(expectedRoom, actualRoom);
    }

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
        return roomService.getRoomByRoomNumber(roomNumber);
    }
}
