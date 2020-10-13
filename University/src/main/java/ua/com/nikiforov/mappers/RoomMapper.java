package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.Room;
import static ua.com.nikiforov.dao.SqlConstants.RoomsTable.*;

public class RoomMapper implements RowMapper<Room> {
    
    @Override
    public Room mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt(ID));
        room.setNumber(resultSet.getInt(ROOM_NUMBER));
        return room;
    }

}
