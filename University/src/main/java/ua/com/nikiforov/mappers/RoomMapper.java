package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.Room;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_ROOM_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_ROOM_NUMBER;

public class RoomMapper implements RowMapper<Room> {
    
    @Override
    public Room mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Room room = new Room();
        room.setNumber(resultSet.getInt(COLUMN_ROOM_ID));
        room.setNumber(resultSet.getInt(COLUMN_ROOM_NUMBER));
        return room;
    }

}
