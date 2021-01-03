package ua.com.nikiforov.dao.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.Room;

import javax.transaction.Transactional;
import java.util.List;

public interface RoomDAO extends JpaRepository<Room, Integer> {

    @Transactional
    public Room save(Room room);

    public Room getRoomById(int id);

    public Room getRoomByRoomNumber(int roomNumber);

    @Query("SELECT r FROM Room r ORDER BY r.roomNumber")
    public List<Room> getAllRooms();

    @Transactional
    public int deleteRoomById(int id);

}
