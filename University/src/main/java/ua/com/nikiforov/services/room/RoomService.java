package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.controllers.dto.RoomDTO;
import ua.com.nikiforov.models.Room;

public interface RoomService {

    public boolean addRoom(int roomNumber, int seatNumber);

    public Room getRoomById(int id);
    
    public Room getRoomByRoomNumber(int roomNumber);

    public List<Room> getAllRooms();

    public boolean updateRoom(RoomDTO room);

    public boolean deleteRoomById(int id);
}
