package ua.com.nikiforov.dao.room;

import java.util.List;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.models.Room;

public interface RoomDAO {
    
    public void addRoom(RoomDTO roomDTO);

    public Room getRoomById(int id);
    
    public Room getRoomByRoomNumber(int roomNumber);

    public List<Room> getAllRooms();

    public void updateRoom(RoomDTO room);

    public void deleteRoomById(int id);

}
