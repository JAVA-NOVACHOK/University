package ua.com.nikiforov.services.room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.dao.room.RoomDAO;
import ua.com.nikiforov.mappers_dto.RoomMapperDTO;
import ua.com.nikiforov.models.Room;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomDAO roomDAO;
    private RoomMapperDTO roomMapper;

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO,RoomMapperDTO roomMapper) {
        this.roomDAO = roomDAO;
        this.roomMapper = roomMapper;
    }

    @Override
    public void addRoom(RoomDTO roomDTO) {
        try {
            roomDAO.addRoom(roomDTO);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException("Error! Duplicate room while adding",e);
        }
    }

    @Override
    public RoomDTO getRoomById(int id) {
        return roomMapper.roomToRoomDTO(roomDAO.getRoomById(id));
    }

    @Override
    public RoomDTO getRoomByRoomNumber(int roomNumber) {
        return roomMapper.roomToRoomDTO(roomDAO.getRoomByRoomNumber(roomNumber));
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

    private List<RoomDTO> getRoomDTOs(List<Room> rooms){
        List<RoomDTO> roomsDTO = new ArrayList<>();
        for(Room room : rooms) {
            roomsDTO.add(roomMapper.roomToRoomDTO(room));
        }
        return roomsDTO;
    }


}
