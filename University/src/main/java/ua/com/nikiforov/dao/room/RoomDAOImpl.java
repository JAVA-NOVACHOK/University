package ua.com.nikiforov.dao.room;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.controllers.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.RoomMapper;
import ua.com.nikiforov.models.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDAOImpl.class);

    private static final String FIND_ROOM_BY_ROOM_NUMBER = "SELECT  r  FROM Room r  WHERE r.roomNumber = ?1 ";
    private static final String GET_ALL_ROOMS = "select r from Room r ORDER BY r.roomNumber";
    private static final String DELETE_ROOM_BY_ID = "DELETE FROM Room  WHERE id =  ?1";

    private RoomMapper roomMapper;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public RoomDAOImpl(DataSource dataSource, RoomMapper roomMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.roomMapper = roomMapper;
    }

    @Override
    @Transactional
    public boolean addRoom(int roomNumber, int seatNumber) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setSeatNumber(seatNumber);
        try {
            entityManager.persist(room);
            entityManager.flush();
        } catch (Throwable e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                String message = String.format("Such room already exists number -'%s'", roomNumber);
                LOGGER.debug(message);
                throw new DuplicateKeyException(message, t);
            }
        }
        return true;
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
                .setParameter(1,roomNumber)
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
            allRooms.addAll(entityManager.createQuery(GET_ALL_ROOMS,Room.class).getResultList());
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
    public boolean updateRoom(RoomDTO room) {
        boolean actionResult = false;
        long id = room.getId();
        int number = room.getRoomNumber();
        int seatNumber = room.getSeatNumber();
        LOGGER.debug("Updating room {}", room);
        try {
            RoomDTO updatedRoom = entityManager.merge(room);
            entityManager.flush();
            actionResult = room.equals(updatedRoom);
            if (actionResult) {
                LOGGER.info("Successfully updated room with id = '{}', number = '{}' and {} seats", id, number,
                        seatNumber);
            } else {
                String failMessage = String.format("Couldn't update Room with id - %d, number - %s, %d seats", id,
                        number, seatNumber);
                throw new DataOperationException(failMessage);
            }
        } catch (PersistenceException e) {
            String failMessage = String.format("Couldn't update Room with id - %d number - %d, %d seats", id, number,
                    seatNumber);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }



    @Override
    @Transactional
    public boolean deleteRoomById(int id) {
        LOGGER.debug("Deleting room by id '{}' ", id);
        boolean actionResult = false;
        String failDeleteMessage = String.format("Couldn't delete Room by ID %d", id);
        try {
            actionResult = entityManager.createQuery(DELETE_ROOM_BY_ID)
                    .setParameter(1, id)
                    .executeUpdate() > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting Room with id '{}'.", id);
            } else {
                throw new EntityNotFoundException(failDeleteMessage);
            }
        } catch (PersistenceException e) {
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;

    }
}



