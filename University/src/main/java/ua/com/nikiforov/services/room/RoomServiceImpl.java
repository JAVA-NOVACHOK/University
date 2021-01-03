package ua.com.nikiforov.services.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.room.RoomDAO;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.RoomMapperDTO;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);

    private RoomDAO roomDAO;
    private RoomMapperDTO roomMapper;

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO, RoomMapperDTO roomMapper) {
        this.roomDAO = roomDAO;
        this.roomMapper = roomMapper;
    }

    @Override
    public void addRoom(RoomDTO roomDTO) {
        LOGGER.debug("Adding {}", roomDTO);
        try {
            roomDAO.save(roomMapper.roomDTOToRoom(roomDTO));
            LOGGER.debug("Successfully added {}", roomDTO);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Couldn't add {}", roomDTO);
            throw new DuplicateKeyException("Error! Duplicate room while adding", e);
        }
    }

    @Override
    public RoomDTO getRoomById(int id) {
        LOGGER.debug("Getting Room by id '{}'", id);
        RoomDTO room = roomMapper.roomToRoomDTO(roomDAO.getRoomById(id));
        if (room == null) {
            String failMessage = String.format("Fail to get room by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved room '{}'", room);
        return room;

    }

    @Override
    public RoomDTO getRoomByRoomNumber(int roomNumber) {
        LOGGER.debug("Getting room by room number '{}'", roomNumber);
        RoomDTO room = roomMapper.roomToRoomDTO(roomDAO.getRoomByRoomNumber(roomNumber));
        if (room == null) {
            String failMessage = String.format("Fail to get roomNumber by roomNumber %d from DB", roomNumber);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved room '{}'", room);
        return room;
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        LOGGER.debug("Getting all rooms");
        List<RoomDTO> allRooms = new ArrayList<>();
        try {
            allRooms.addAll(roomMapper.roomsToRoomsDTO(roomDAO.getAllRooms()));
            LOGGER.info("Successfully query for all rooms");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all rooms from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allRooms;
    }

    @Override
    public void updateRoom(RoomDTO roomDTO) {
        String updateMessage = String.format("Room with id = '%d', number = '%d' and '%d' seats", roomDTO.getId(),
                roomDTO.getRoomNumber(), roomDTO.getSeatNumber());
        LOGGER.debug("Updating {}", updateMessage);
        try {
            roomDAO.save(roomMapper.roomDTOToRoom(roomDTO));
        } catch (DataIntegrityViolationException e) {
            String failMessage = String.format("ERROR! Couldn't update %s", updateMessage);
            LOGGER.error(failMessage);
            throw new DuplicateKeyException(failMessage, e);
        }
    }

    @Override
    public boolean deleteRoomById(int id) {
        String deleteMessage = String.format("Room by ID %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        boolean actionResult = false;
        actionResult = roomDAO.deleteRoomById(id) > 0;
        if (!actionResult) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage);
        }
        return actionResult;
    }
}
