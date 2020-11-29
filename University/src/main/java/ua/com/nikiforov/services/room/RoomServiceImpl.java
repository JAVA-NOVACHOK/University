package ua.com.nikiforov.services.room;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.RoomDTO;
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
    public boolean addRoom(int roomNumber, int seatNumber) {
        return roomDAO.addRoom(roomNumber, seatNumber);
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
        List<Room> rooms = roomDAO.getAllRooms();
        Collections.sort(rooms,(r1,r2) -> r1.compareTo(r2));
        return rooms;

    }

    @Override
    public boolean updateRoom(RoomDTO room) {
        return roomDAO.updateRoom(room);
    }

    @Override
    public boolean deleteRoomById(int id) {
        return roomDAO.deleteRoomById(id);
    }

}
