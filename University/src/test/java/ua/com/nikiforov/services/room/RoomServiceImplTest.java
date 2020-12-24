package ua.com.nikiforov.services.room;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class RoomServiceImplTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;

    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddRoomIfSuccessReturnTrue() {
        assertDoesNotThrow(() -> roomService.addRoom(new RoomDTO(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1)));
    }

    @Test
    void afterAddRoomGetRoomByIdReturnCorrectRoom() {
        RoomDTO room = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        assertEquals(room, roomService.getRoomById(room.getId()));
    }

    @Test
    void whenGetAllRoomsThenReturnGroupList() {
        List<RoomDTO> expectedRooms = new ArrayList<>();
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1));
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2));
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_3));
        assertIterableEquals(expectedRooms, roomService.getAllRooms());
    }

    @Test
    void whenUpdateRoomThenGroupHasUpdatedName() {
        RoomDTO room = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        int roomId = room.getId();
        roomService.updateRoom(new RoomDTO(roomId,TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_2));
        RoomDTO expectedRoom = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_2);
        RoomDTO actualRoom = roomService.getRoomById(roomId);
        assertEquals(expectedRoom, actualRoom);
    }

    @Test
    void whenDeleteRoomByIdIfSuccessThenReturnTrue() {
        RoomDTO room = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        assertDoesNotThrow(() -> roomService.deleteRoomById(room.getId()));
    }

    @Test
    void afterDeleteRoomByIdIfSearchReturnEntityNotFoundException() {
        RoomDTO room = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        int roomId = room.getId();
        roomService.deleteRoomById(roomId);
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoomById(roomId));
    }

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
        return roomService.getRoomByRoomNumber(roomNumber);
    }
}
