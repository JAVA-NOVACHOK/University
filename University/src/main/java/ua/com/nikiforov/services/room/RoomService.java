package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.dto.RoomDTO;

public interface RoomService {

    public RoomDTO addRoom(RoomDTO roomDTO);

    public RoomDTO getRoomById(int id);
    
    public RoomDTO getRoomByRoomNumber(int roomNumber);

    public List<RoomDTO> getAllRooms();

    public RoomDTO updateRoom(RoomDTO room);

    public void deleteRoomById(int id);
}
