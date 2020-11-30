package ua.com.nikiforov.dao.room;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    private static final String ADD_ROOM = "INSERT INTO rooms (room_number,seat_number) VALUES(?,?)";
    private static final String FIND_ROOM_BY_ID = "SELECT  *  FROM rooms  WHERE room_id =  ? ";
    private static final String FIND_ROOM_BY_ROOM_NUMBER = "SELECT  *  FROM rooms  WHERE room_number =  ? ";
    private static final String GET_ALL_ROOMS = "SELECT  *  FROM rooms ORDER BY room_number";
    private static final String UPDATE_ROOM = "UPDATE rooms  SET room_number =  ? ,seat_number =  ?  WHERE room_id =  ? ";
    private static final String DELETE_ROOM_BY_ID = "DELETE FROM rooms  WHERE room_id =  ? ";

    private RoomMapper roomMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDAOImpl(DataSource dataSource, RoomMapper roomMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.roomMapper = roomMapper;
    }

    @Override
    public boolean addRoom(int roomNumber, int seatNumber) {
        String roomMessage = String.format("Room with number '%d' with '%d' seats", roomNumber, seatNumber);
        LOGGER.debug("Adding {} ", roomMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_ROOM, roomNumber, seatNumber) > 0;
            if (actionResult) {
                LOGGER.info("Successfully added {}", roomMessage);
            } else {
                String failMessage = String.format("Fail to add %s", roomMessage);
                throw new DataOperationException(failMessage);
            }
        }catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Room with name " + roomNumber +" already exists",e);
        }catch (DataAccessException e) {
            String message = String.format("Couldn't add %s to DB", roomMessage);
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
        } catch (DataAccessException e) {
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
        long id =  room.getId();
        int number = room.getRoomNumber();
        int seatNumber = room.getSeatNumber();
        LOGGER.debug("Updating room with id - '{}' to number - '{}'", id, number);
        try {
            actionResult = jdbcTemplate.update(UPDATE_ROOM,number ,seatNumber , id) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated room with id = '{}', number = '{}' and {} seats", id, number,
                        seatNumber);
            } else {
                String failMessage = String.format("Couldn't update Room with id - %d, number - %s, %d seats", id,
                        number, seatNumber);
                throw new DataOperationException(failMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Couldn't update Room with id - %d number - %d, %d seats", id, number,
                    seatNumber);
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
