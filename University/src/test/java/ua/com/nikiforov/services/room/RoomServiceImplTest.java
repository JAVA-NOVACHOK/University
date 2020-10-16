package ua.com.nikiforov.services.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Room;

@SpringJUnitConfig(UniversityConfig.class)
class RoomServiceImplTest {

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;
    private static final int TEST_ROOM_COUNT = 3;

    private static final String SPACE = " ";

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
    void whenQueryRoomByIdThenReturnSearchingRoom() {
        Room room = insertRoom(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        assertEquals(TEST_ROOM_NUMBER_1, room.getNumber());
        assertEquals(roomId, roomService.getRoomById(roomId).getId());
    }

    @Test
    void whenGetAllRoomsThenReturnGroupList() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        roomService.addRoom(TEST_ROOM_NUMBER_2);
        roomService.addRoom(TEST_ROOM_NUMBER_3);
        StringBuilder expectedRoomNumbers = new StringBuilder();
        expectedRoomNumbers.append(TEST_ROOM_NUMBER_1).append(SPACE).append(TEST_ROOM_NUMBER_2).append(SPACE)
                .append(TEST_ROOM_NUMBER_3).append(SPACE);
        StringBuilder actualRoomNumbers = new StringBuilder();
        long countRoom = roomService.getAllRooms().stream()
                .map(r -> actualRoomNumbers.append(r.getNumber()).append(SPACE)).count();
        assertEquals(expectedRoomNumbers.toString(), actualRoomNumbers.toString());
        assertEquals(TEST_ROOM_COUNT, countRoom);
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
        assertEquals(TEST_ROOM_NUMBER_2, roomService.getRoomById(roomId).getNumber());
        assertEquals(roomId, roomService.getRoomById(roomId).getId());
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
        assertThrows(EmptyResultDataAccessException.class, () -> roomService.getRoomById(roomId));
    }

    private Room insertRoom(int roomNumber) {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        return roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
    }
}
