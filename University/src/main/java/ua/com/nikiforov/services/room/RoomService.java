<<<<<<< HEAD
package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.models.Room;

public interface RoomService {

    public boolean addRoom(int groupNumber, int seatNumber);

    public Room getRoomById(int id);
    
    public Room getRoomByRoomNumber(int roomNumber);

    public List<Room> getAllRooms();

    public boolean updateRoom(int number, int seatNumber, int id);

    public boolean deleteRoomById(int id);
}
=======
package ua.com.nikiforov.services.room;

import java.util.List;

import ua.com.nikiforov.models.Room;

public interface RoomService {

    public boolean addRoom(int groupNumber);

    public Room getRoomById(int id);
    
    public Room getRoomByRoomNumber(int roomNumber);

    public List<Room> getAllRooms();

    public boolean updateRoom(int number, int id);

    public boolean deleteRoomById(int id);
}
>>>>>>> refs/remotes/origin/master
