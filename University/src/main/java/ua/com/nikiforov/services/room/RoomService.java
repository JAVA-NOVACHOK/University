package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.dto.RoomDTO;

public interface RoomService {

    public void addRoom(RoomDTO roomDTO);

    public RoomDTO getRoomById(int id);
    
    public RoomDTO getRoomByRoomNumber(int roomNumber);

    public List<RoomDTO> getAllRooms();

    public void updateRoom(RoomDTO room);

    public boolean deleteRoomById(int id);
}
