package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Room;
import static ua.com.nikiforov.dao.SqlConstants.RoomsTable.*;

@Component
public class RoomMapper implements RowMapper<Room> {
    
    @Override
    public Room mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt(ID));
        room.setRoomNumber(resultSet.getInt(ROOM_NUMBER));
        room.setSeatNumber(resultSet.getInt(SEAT_NUMBER));
        return room;
    }

}
