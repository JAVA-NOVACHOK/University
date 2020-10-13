package ua.com.nikiforov.dao.room;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.RoomsTable.*;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.RoomMapper;
import ua.com.nikiforov.models.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final String ADD_ROOM = INSERT + TABLE_ROOMS + L_BRACKET + ROOM_NUMBER + VALUES_1_QMARK;
    private static final String FIND_ROOM_BY_ID = SELECT + ASTERISK + FROM + TABLE_ROOMS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_ROOM_BY_ROOM_NUMBER = SELECT + ASTERISK + FROM + TABLE_ROOMS + WHERE + ROOM_NUMBER
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_ROOMS = SELECT + ASTERISK + FROM + TABLE_ROOMS;
    private static final String UPDATE_ROOM = UPDATE + TABLE_ROOMS + SET + ROOM_NUMBER + EQUALS_M + Q_MARK + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String DELETE_ROOM_BY_ID = DELETE + FROM + TABLE_ROOMS + WHERE + ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addRoom(int groupNumber) {
        return jdbcTemplate.update(ADD_ROOM, groupNumber) > 0;
    }

    @Override
    public Room getRoomById(int id) {
        return jdbcTemplate.queryForObject(FIND_ROOM_BY_ID, new Object[] { id }, new RoomMapper());
    }

    @Override
    public Room getRoomByRoomNumber(int roomNumber) {
        return jdbcTemplate.queryForObject(FIND_ROOM_BY_ROOM_NUMBER, new Object[] { roomNumber }, new RoomMapper());
    }

    @Override
    public List<Room> getAllRooms() {
        return jdbcTemplate.query(GET_ALL_ROOMS, new RoomMapper());
    }

    @Override
    public boolean updateRoom(int number, int id) {
        return jdbcTemplate.update(UPDATE_ROOM, number, id) > 0;
    }

    @Override
    public boolean deleteRoomById(int id) {
        return jdbcTemplate.update(DELETE_ROOM_BY_ID, id) > 0;
    }

}
