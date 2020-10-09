package ua.com.nikiforov.services.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.room.RoomDAOImpl;
import ua.com.nikiforov.models.Room;

@Service
public class RoomServiceImpl implements RoomService {
    
    @Autowired
    private RoomDAOImpl roomDAOImpl;

    @Override
    public boolean addRoom(int groupNumber) {
       return roomDAOImpl.addRoom(groupNumber);
    }

    @Override
    public Room getRoomById(int id) {
        return roomDAOImpl.getRoomById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomDAOImpl.getAllRooms();
    }

    @Override
    public boolean updateRoom(int number, int id) {
        return roomDAOImpl.updateRoom(number, id);
    }

    @Override
    public boolean deleteRoomById(int id) {
        return roomDAOImpl.deleteRoomById(id);
    }

}
