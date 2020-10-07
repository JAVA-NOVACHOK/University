package ua.com.nikiforov.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.RoomMapper;
import ua.com.nikiforov.models.Room;
import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;

public class RoomDAO {

    private static final String ADD_ROOM = INSERT + TABLE_ROOMS + L_BRACKET + COLUMN_ROOM_NUMBER + VALUES_1_QMARK;
    private static final String FIND_ROOM_BY_ID = SELECT + ASTERISK + FROM + TABLE_ROOMS + WHERE + COLUMN_ROOM_ID
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_ROOMS = SELECT + ASTERISK + FROM + TABLE_ROOMS;
    private static final String UPDATE_ROOM = UPDATE + TABLE_ROOMS + SET + COLUMN_ROOM_NUMBER + EQUALS_M + Q_MARK
            + WHERE + COLUMN_ROOM_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_ROOM_BY_ID = DELETE + FROM + TABLE_ROOMS + WHERE + COLUMN_ROOM_ID + EQUALS_M
            + Q_MARK;
    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addRoom(int groupNumber) {
        return jdbcTemplate.update(ADD_ROOM, groupNumber) > 0;
    }

    public Room findRoomById(int id) {
        return jdbcTemplate.queryForObject(FIND_ROOM_BY_ID, new Object[] { id }, new RoomMapper());
    }

    public List<Room> getAllRooms() {
        return jdbcTemplate.query(GET_ALL_ROOMS, new RoomMapper());
    }

    public boolean updateRoom(int number, int id) {
        return jdbcTemplate.update(UPDATE_ROOM, number, id) > 0;
    }

    public boolean deleteRoomById(int id) {
        return jdbcTemplate.update(DELETE_ROOM_BY_ID, id) > 0;
    }

}
