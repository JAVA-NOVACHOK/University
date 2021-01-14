package ua.com.nikiforov.repositories.room;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    public Optional<Room> getRoomByRoomNumber(int roomNumber);

}
