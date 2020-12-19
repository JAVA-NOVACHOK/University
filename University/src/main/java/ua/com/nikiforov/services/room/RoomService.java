package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.dto.RoomDTO;

public interface RoomService {

    public void addRoom(int roomNumber, int seatNumber);

    public RoomDTO getRoomById(int id);
    
    public RoomDTO getRoomByRoomNumber(int roomNumber);

    public List<RoomDTO> getAllRooms();

    public void updateRoom(RoomDTO room);

    public void deleteRoomById(int id);
}
