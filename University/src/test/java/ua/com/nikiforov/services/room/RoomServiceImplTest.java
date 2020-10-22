package ua.com.nikiforov.services.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Room;

@SpringJUnitConfig(UniversityConfig.class)
class RoomServiceImplTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;


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
        assertTrue(roomService.addRoom(TEST_ROOM_NUMBER_1));
    }

    
    @Test
    void afterAddRoomGetRoomByIdReturnCorrectRoom() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        assertEquals(room, roomService.getRoomById(room.getId()));
    }

    @Test
    void whenGetAllRoomsThenReturnGroupList() {
        List<Room> expectedRooms = new ArrayList<>();
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_1));
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_2));
        expectedRooms.add(insertRoom(TEST_ROOM_NUMBER_3));
        assertIterableEquals(expectedRooms,roomService.getAllRooms());
    }

    @Test
    void whenUpdateRoomByIdIfSuccessThenReturnTrue() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        assertTrue(roomService.updateRoom(TEST_ROOM_NUMBER_2, room.getId()));
    }

    @Test
    void whenUpdateRoomIfSuccessThenGetGroupByIdAfterUpdateReturnChangedName() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        roomService.updateRoom(TEST_ROOM_NUMBER_2, roomId);
        Room expectedRoom = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_2);
        Room actualRoom = roomService.getRoomById(roomId);
        assertEquals(expectedRoom, actualRoom);
    }

    @Test
    void whenDeleteRoomByIdIfSuccessThenReturnTrue() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        assertTrue(roomService.deleteRoomById(room.getId()));
    }

    @Test
    void afterDeleteRoomByIdIfSearchForItReturnEmptyResultDataAccessException() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        roomService.deleteRoomById(roomId);
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoomById(roomId));
    }

    private Room insertRoom(int roomNumber) {
        roomService.addRoom(roomNumber);
        return roomService.getRoomByRoomNumber(roomNumber);
    }
}
