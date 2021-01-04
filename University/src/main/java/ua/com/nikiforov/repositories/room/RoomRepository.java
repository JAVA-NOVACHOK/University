package ua.com.nikiforov.repositories.room;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    public Room getRoomByRoomNumber(int roomNumber);

}
