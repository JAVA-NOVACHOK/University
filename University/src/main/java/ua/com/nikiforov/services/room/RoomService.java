package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.models.Room;

public interface RoomService {

    public boolean addRoom(int groupNumber);

    public Room getRoomById(int id);

    public List<Room> getAllRooms();

    public boolean updateRoom(int number, int id);

    public boolean deleteRoomById(int id);
}
