package ua.com.nikiforov.dao.room;

import java.util.List;
import ua.com.nikiforov.models.Room;

public interface RoomDAO {
    
    public boolean addRoom(int groupNumber);

    public Room getRoomById(int id);
    
    public Room getRoomByRoomNumber(int roomNumber);

    public List<Room> getAllRooms();

    public boolean updateRoom(int number, int id);

    public boolean deleteRoomById(int id);

}
