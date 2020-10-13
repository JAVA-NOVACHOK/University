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
import ua.com.nikiforov.dao.tablecreator.TableCreator;
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
    void whenAddRoomCanGetRoomByNumber() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        assertEquals(TEST_ROOM_NUMBER_1, room.getNumber());
    }

    @Test
    void whenQueryRoomByIdThenReturnSearchingRoom() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        System.out.println(room.getId() + SPACE + room.getNumber());
        assertEquals(roomId, roomService.getRoomById(roomId).getId());
    }

    @Test
    void whenGetAllRoomsThenReturnGroupList() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        roomService.addRoom(TEST_ROOM_NUMBER_2);
        roomService.addRoom(TEST_ROOM_NUMBER_3);
        StringBuilder expected = new StringBuilder();
        expected.append(TEST_ROOM_NUMBER_1).append(SPACE).append(TEST_ROOM_NUMBER_2).append(SPACE)
                .append(TEST_ROOM_NUMBER_3).append(SPACE);
        StringBuilder actual = new StringBuilder();
        long count = roomService.getAllRooms().stream().map(r -> actual.append(r.getNumber()).append(SPACE)).count();
        assertEquals(expected.toString(), actual.toString());
        assertEquals(TEST_ROOM_COUNT, count);
    }

    @Test
    void whenUpdateRoomByIdIfSuccessThenReturnTrue() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        assertTrue(roomService.updateRoom(TEST_ROOM_NUMBER_2, room.getId()));
    }

    @Test
    void whenUpdateRoomIfSuccessThenGetGroupByIdAfterUpdateReturnChangedName() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        assertEquals(TEST_ROOM_NUMBER_2, roomService.getRoomById(roomId).getNumber());
    }

    @Test
    void whenDeleteRoomByIdIfSuccessThenReturnTrue() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        assertTrue(roomService.deleteRoomById(room.getId()));
    }

    @Test
    void afterDeleteRoomByIdIfSearchForItReturnEmptyResultDataAccessException() {
        roomService.addRoom(TEST_ROOM_NUMBER_1);
        Room room = roomService.getRoomByRoomNumber(TEST_ROOM_NUMBER_1);
        int roomId = room.getId();
        roomService.deleteRoomById(roomId);
        assertThrows(EmptyResultDataAccessException.class, () -> roomService.getRoomById(roomId));
    }
}
