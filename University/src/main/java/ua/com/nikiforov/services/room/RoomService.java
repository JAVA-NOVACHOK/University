package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.controllers.dto.RoomDTO;
import ua.com.nikiforov.models.Room;

public interface RoomService {

    public boolean addRoom(int roomNumber, int seatNumber);

    public RoomDTO getRoomById(int id);
    
    public RoomDTO getRoomByRoomNumber(int roomNumber);

    public List<RoomDTO> getAllRooms();

    public boolean updateRoom(RoomDTO room);

    public boolean deleteRoomById(int id);
}
