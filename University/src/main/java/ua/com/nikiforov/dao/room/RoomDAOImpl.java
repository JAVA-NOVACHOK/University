package ua.com.nikiforov.dao.room;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.RoomsTable.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.RoomMapper;
import ua.com.nikiforov.models.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDAOImpl.class);

    private static final String ADD_ROOM = INSERT + TABLE_ROOMS + L_BRACKET + ROOM_NUMBER + COMA + SEAT_NUMBER + VALUES_2_QMARK;
    private static final String FIND_ROOM_BY_ID = SELECT + ASTERISK + FROM + TABLE_ROOMS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_ROOM_BY_ROOM_NUMBER = SELECT + ASTERISK + FROM + TABLE_ROOMS + WHERE + ROOM_NUMBER
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_ROOMS = SELECT + ASTERISK + FROM + TABLE_ROOMS;
    private static final String UPDATE_ROOM = UPDATE + TABLE_ROOMS + SET + ROOM_NUMBER + EQUALS_M + Q_MARK + COMA + SEAT_NUMBER + EQUALS_M + Q_MARK + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String DELETE_ROOM_BY_ID = DELETE + FROM + TABLE_ROOMS + WHERE + ID + EQUALS_M + Q_MARK;

    private RoomMapper roomMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDAOImpl(DataSource dataSource, RoomMapper roomMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.roomMapper = roomMapper;
    }

    @Override
    public boolean addRoom(int roomNumber, int seatNumber) {
        LOGGER.debug("Adding Room with number '{}' with {} seats", roomNumber, seatNumber);
        boolean actionResult = jdbcTemplate.update(ADD_ROOM, roomNumber,seatNumber) > 0;
        try {
            if (actionResult) {
                LOGGER.info("Successful adding room '{}' ", roomNumber);
            } else {
                String failMessage = String.format("Fail to add room with number %d", roomNumber);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String message = String.format("Couldn't add Room number %d to DB", roomNumber);
            LOGGER.error(message);
            throw new DataOperationException(message, e);
        }
        return true;
    }

    @Override
    public Room getRoomById(int id) {
        LOGGER.debug("Getting Room by id '{}'", id);
        Room room;
        try {
            room = jdbcTemplate.queryForObject(FIND_ROOM_BY_ID, new Object[] { id }, roomMapper);
            LOGGER.info("Successfully retrieved room '{}'", room);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Fail to get room by Id %d from DB", id);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage, e);
        }
        return room;
    }

    @Override
    public Room getRoomByRoomNumber(int roomNumber) {
        LOGGER.debug("Getting room by room number '{}'", roomNumber);
        Room room;
        try {
            room = jdbcTemplate.queryForObject(FIND_ROOM_BY_ROOM_NUMBER, new Object[] { roomNumber }, roomMapper);
            LOGGER.info("Successfully retrieved room '{}'", room);
        } catch (EmptyResultDataAccessException e) {
            String failMessage = String.format("Fail to get room by  room number %d from DB", roomNumber);
            LOGGER.error(failMessage);
            throw new EntityNotFoundException(failMessage, e);
        }
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        LOGGER.debug("Getting all rooms");
        List<Room> allRooms = new ArrayList<>();
        try {
            allRooms.addAll(jdbcTemplate.query(GET_ALL_ROOMS, roomMapper));
            LOGGER.info("Successfully query for all rooms");
        }catch (DataAccessException e) {
            String failMessage = "Fail to get all rooms from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage,e);
        }
        return allRooms;
    }

    @Override
    public boolean updateRoom(int number, int seatNumber, int id) {
        LOGGER.debug("Updating room with id - '{}' to number - '{}'", id, number);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_ROOM, number, seatNumber, id) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated room with id = '{}', number = '{}' and {} seats", id, number,seatNumber);
            } else {
                String failMessage = String.format("Couldn't update Room with id - %d, number - %s, %d seats",id, number,seatNumber);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't update Room with id - %d number - %d, %d seats",id, number, seatNumber);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteRoomById(int id) {
        LOGGER.debug("Deleting room by id '{}' ", id);
        boolean actionResult = false;
        String failDeleteMessage = String.format("Couldn't delete Room by ID %d", id);
        try {
            actionResult = jdbcTemplate.update(DELETE_ROOM_BY_ID, id) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting Room with id '{}'.", id);
            } else {
                throw new EntityNotFoundException(failDeleteMessage);
            }
        } catch (DataAccessException e) {
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }

}
