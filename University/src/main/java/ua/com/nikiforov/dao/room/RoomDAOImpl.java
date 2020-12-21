package ua.com.nikiforov.dao.room;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDAOImpl.class);

    private static final String FIND_ROOM_BY_ROOM_NUMBER = "SELECT  r  FROM Room r  WHERE r.roomNumber = ?1 ";
    private static final String GET_ALL_ROOMS = "select r from Room r ORDER BY r.roomNumber";
    private static final String DELETE_ROOM_BY_ID = "DELETE FROM Room  WHERE id =  ?1";

    private static final int FIRST_PARAMETER_INDEX = 1;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional()
    public void addRoom(int roomNumber, int seatNumber) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setSeatNumber(seatNumber);
        entityManager.persist(room);
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
        Room room = (Room) entityManager.createQuery(FIND_ROOM_BY_ROOM_NUMBER)
                .setParameter(FIRST_PARAMETER_INDEX, roomNumber)
                .getSingleResult();
        if (room == null) {
            String failMessage = String.format("Fail to get roomNumber by roomNumber %d from DB", roomNumber);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage);
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
    public void updateRoom(RoomDTO room) {
        int id = room.getId();
        int number = room.getRoomNumber();
        int seatNumber = room.getSeatNumber();
        LOGGER.debug("Updating room {}", room);
        try {
            entityManager.merge(new Room(id, number, seatNumber));
            LOGGER.info("Successfully updated room with id = '{}', number = '{}' and {} seats", id, number,
                    seatNumber);
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Room with id - %d number - %d, %d seats", id, number,
                    seatNumber);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
    }


    @Override
    @Transactional
    public void deleteRoomById(int id) {
        LOGGER.debug("Deleting room by id '{}' ", id);
        String failDeleteMessage = String.format("Couldn't delete Room by ID %d", id);
        try {
             entityManager.createQuery(DELETE_ROOM_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, id)
                    .executeUpdate() ;
                LOGGER.info("Successful deleting Room with id '{}'.", id);
        } catch (PersistenceException e) {
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }
}



