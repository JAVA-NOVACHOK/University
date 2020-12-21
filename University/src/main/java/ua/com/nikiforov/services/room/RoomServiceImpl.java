package ua.com.nikiforov.services.room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.RoomDTO;
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
    public void addRoom(int roomNumber, int seatNumber) {
        try {
            roomDAO.addRoom(roomNumber, seatNumber);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException("Error! Duplicate room while adding",e);
        }
    }

    @Override
    public RoomDTO getRoomById(int id) {
        return getRoomDTO(roomDAO.getRoomById(id));
    }

    @Override
    public RoomDTO getRoomByRoomNumber(int roomNumber) {
        return getRoomDTO(roomDAO.getRoomByRoomNumber(roomNumber));
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return getRoomDTOs(roomDAO.getAllRooms());
    }

    @Override
    public void updateRoom(RoomDTO room) {
        try {
            roomDAO.updateRoom(room);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException("Error! Duplicate room while editing!",e);
        }
    }

    @Override
    public void deleteRoomById(int id) {
         roomDAO.deleteRoomById(id);
    }

    private static List<RoomDTO> getRoomDTOs(List<Room> rooms){
        List<RoomDTO> roomsDTO = new ArrayList<>();
        for(Room room : rooms) {
            roomsDTO.add(getRoomDTO(room));
        }
        return roomsDTO;
    }

    public static RoomDTO getRoomDTO(Room room) {
        int id = room.getId();
        int number = room.getRoomNumber();
        int seats = room.getSeatNumber();
        return new RoomDTO(id, number, seats);
    }

}
