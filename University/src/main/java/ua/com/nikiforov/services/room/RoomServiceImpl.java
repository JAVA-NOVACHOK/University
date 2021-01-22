package ua.com.nikiforov.services.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.repositories.room.RoomRepository;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.RoomMapperDTO;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);

    private static final Sort SORT_BY_ROOM_NUMBER = Sort.by(Sort.Direction.ASC, "roomNumber");

    private static final String GETTING = "Getting '{}'";

    private RoomRepository roomRepository;
    private RoomMapperDTO roomMapper;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapperDTO roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public RoomDTO addRoom(RoomDTO roomDTO) {
        LOGGER.debug("Adding {}", roomDTO);
        Room room;
        try {
           room = roomRepository.save(roomMapper.roomDTOToRoom(roomDTO));
            LOGGER.debug("Successfully added {}", roomDTO);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Couldn't add {}", roomDTO);
            throw new DuplicateKeyException("Error! Duplicate room while adding", e);
        }
        return roomMapper.roomToRoomDTO(room);
    }

    @Override
    @Transactional
    public RoomDTO getRoomById(int id) {
        String getMessage = String.format("Room by id %d", id);
        LOGGER.debug(GETTING, getMessage);
        Room room;
        try {
            room = roomRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            String failMessage = String.format("Couldn't get %s", getMessage);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved room '{}'", getMessage);
        return roomMapper.roomToRoomDTO(room);
    }


    @Override
    public RoomDTO getRoomByRoomNumber(int roomNumber) {
        LOGGER.debug("Getting room by room number '{}'", roomNumber);
        Room room = roomRepository.getRoomByRoomNumber(roomNumber).orElseThrow(EntityNotFoundException::new);
        if (room == null) {
            String failMessage = String.format("Fail to get roomNumber by roomNumber %d from DB", roomNumber);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved room '{}'", room);
        return roomMapper.roomToRoomDTO(room);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        LOGGER.debug("Getting all rooms");
        List<RoomDTO> allRooms = new ArrayList<>();
        try {
            allRooms.addAll(roomMapper.roomsToRoomsDTO(roomRepository.findAll(SORT_BY_ROOM_NUMBER)));
            LOGGER.info("Successfully query for all rooms");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all rooms from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allRooms;
    }

    @Override
    @Transactional
    public RoomDTO updateRoom(RoomDTO roomDTO) {
        String updateMessage = String.format("Room with id = '%d', number = '%d' and '%d' seats", roomDTO.getId(),
                roomDTO.getRoomNumber(), roomDTO.getSeatNumber());
        LOGGER.debug("Updating {}", updateMessage);
        Room room;
        try {
            room = roomRepository.save(roomMapper.roomDTOToRoom(roomDTO));
        } catch (DataIntegrityViolationException e) {
            String failMessage = String.format("ERROR! Couldn't update %s", updateMessage);
            LOGGER.error(failMessage);
            throw new DuplicateKeyException(failMessage, e);
        }
        return roomMapper.roomToRoomDTO(room);
    }

    @Override
    @Transactional
    public void deleteRoomById(int id) {
        String deleteMessage = String.format("Room by ID %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        try {
            roomRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Couldn't get %s",deleteMessage));
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }
}
