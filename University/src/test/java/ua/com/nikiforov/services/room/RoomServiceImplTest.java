package ua.com.nikiforov.services.room;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.exceptions.EntityNotFoundException;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestDataSource.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    RoomDTO room_1;
    RoomDTO room_2;
    RoomDTO room_3;
    RoomDTO room_4;
    RoomDTO room_5;
    RoomDTO room_6;
    RoomDTO room_7;

    @Autowired
    private RoomServiceImpl roomService;

    @BeforeAll
    void setup(){
         room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
         room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
         room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);
         room_4 = insertRoom(TEST_ROOM_NUMBER_4, TEST_SEAT_NUMBER_1);
         room_5 = insertRoom(TEST_ROOM_NUMBER_5, TEST_SEAT_NUMBER_1);
         room_6 = insertRoom(TEST_ROOM_NUMBER_6, TEST_SEAT_NUMBER_1);
         room_7 = insertRoom(TEST_ROOM_NUMBER_7, TEST_SEAT_NUMBER_1);
    }

    @Test
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
        LOGGER.debug("Actual rooms +++++++++++++++++++++++++ {}",actualRooms);
        assertIterableEquals(expectedRooms, actualRooms);
    }

    @Test
    void afterAddRoomGetRoomByIdReturnCorrectRoom() {
        assertEquals(room_2, roomService.getRoomById(room_2.getId()));
    }

    @Test
    void whenUpdateRoomThenGroupHasUpdatedName() {
        int roomId = room_7.getId();
        roomService.updateRoom(new RoomDTO(roomId,TEST_ROOM_NUMBER_8, TEST_SEAT_NUMBER_2));
        RoomDTO expectedRoom = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_8);
        RoomDTO actualRoom = roomService.getRoomById(roomId);
        assertEquals(expectedRoom, actualRoom);
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

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
        return roomService.getRoomByRoomNumber(roomNumber);
    }
}
