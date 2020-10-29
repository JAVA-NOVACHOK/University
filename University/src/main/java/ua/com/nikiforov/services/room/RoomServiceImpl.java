package ua.com.nikiforov.services.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.room.RoomDAO;
import ua.com.nikiforov.models.Room;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomDAO roomDAO;

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public boolean addRoom(int groupNumber, int seatNumber) {
        return roomDAO.addRoom(groupNumber,seatNumber);
    }

    @Override
    public Room getRoomById(int id) {
        return roomDAO.getRoomById(id);
    }

    @Override
    public Room getRoomByRoomNumber(int roomNumber) {
        return roomDAO.getRoomByRoomNumber(roomNumber);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    @Override
    public boolean updateRoom(int number, int seatNumber, int id) {
        return roomDAO.updateRoom(number,seatNumber, id);
    }

    @Override
    public boolean deleteRoomById(int id) {
        return roomDAO.deleteRoomById(id);
    }

}
