package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Room;

@Component
public class RoomMapper implements RowMapper<Room> {
    
    private static final int ROOM_ID_INDEX = 1;
    private static final int ROOM_NUMBER_INDEX = 2;
    private static final int ROOM_SEATS_NUMBER_INDEX = 3;
    
    @Override
    public Room mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt(ROOM_ID_INDEX));
        room.setRoomNumber(resultSet.getInt(ROOM_NUMBER_INDEX));
        room.setSeatNumber(resultSet.getInt(ROOM_SEATS_NUMBER_INDEX));
        return room;
    }

}
