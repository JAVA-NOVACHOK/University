package ua.com.nikiforov.dao.room;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.RoomMapperDTO;
import ua.com.nikiforov.models.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDAOImpl.class);

    private static final String FIND_ROOM_BY_ROOM_NUMBER = "SELECT  r  FROM Room r  WHERE r.roomNumber = ?1 ";
    private static final String GET_ALL_ROOMS = "select r from Room r ORDER BY r.roomNumber";
    private static final String DELETE_ROOM_BY_ID = "DELETE FROM Room  WHERE id =  ?1";

    private static final int FIRST_PARAMETER_INDEX = 1;

    private RoomMapperDTO roomMapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public RoomDAOImpl(RoomMapperDTO roomMapper) {
        this.roomMapper = roomMapper;
    }

    @Override
    @Transactional()
    public void addRoom(RoomDTO roomDTO) {
        entityManager.persist(roomMapper.roomDTOToRoom(roomDTO));
    }

    @Override
    public Room getRoomById(int id) {
        LOGGER.debug("Getting Room by id '{}'", id);
        Room room = entityManager.find(Room.class, id);
        if (room == null) {
            String failMessage = String.format("Fail to get room by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
        }
        LOGGER.info("Successfully retrieved room '{}'", room);
        return room;
    }

    @Override
    public Room getRoomByRoomNumber(int roomNumber) {
        LOGGER.debug("Getting room by room number '{}'", roomNumber);
        Room room;
        try{
        room = (Room) entityManager.createQuery(FIND_ROOM_BY_ROOM_NUMBER)
                .setParameter(FIRST_PARAMETER_INDEX, roomNumber)
                .getSingleResult();
        }catch (NoResultException e){
            String failMessage = String.format("Fail to get roomNumber by roomNumber %d from DB", roomNumber);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage,e);
        }
        LOGGER.info("Successfully retrieved room '{}'", room);
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        LOGGER.debug("Getting all rooms");
        List<Room> allRooms = new ArrayList<>();
        try {
            allRooms.addAll(entityManager.createQuery(GET_ALL_ROOMS, Room.class).getResultList());
            LOGGER.info("Successfully query for all rooms");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all rooms from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        Collections.sort(allRooms);
        return allRooms;
    }

    @Override
    @Transactional
    public void updateRoom(RoomDTO roomDTO) {
        String updateMessage = String.format("Room with id = '%d', number = '%d' and '%d' seats", roomDTO.getId(),
                roomDTO.getRoomNumber(), roomDTO.getSeatNumber());
        LOGGER.debug("Updating {}", updateMessage);
        try {
            entityManager.merge(roomMapper.roomDTOToRoom(roomDTO));
            LOGGER.info("Successfully updated  {} ", updateMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update %s", updateMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }

    @Override
    @Transactional
    public void deleteRoomById(int id) {
        String deleteMessage = String.format("Room by ID %d", id);
        LOGGER.debug("Deleting {}", deleteMessage);
        boolean actionResult = false;
        try {
            actionResult = entityManager.createQuery(DELETE_ROOM_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, id)
                    .executeUpdate() > 0;
            if(!actionResult){
                throw new PersistenceException("Didn't delete room!");
            }
            LOGGER.info("Successful deleting '{}'.", deleteMessage);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't delete %s", deleteMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }
}



