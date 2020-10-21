package ua.com.nikiforov.dao.lesson;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.LessonsTable.*;

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

import ua.com.nikiforov.dao.group.GroupDAOImpl;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.LessonMapper;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Lesson;
import ua.com.nikiforov.models.Room;

@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAOImpl.class);

    private static final String ADD_LESSON = INSERT + TABLE_LESSONS + L_BRACKET + GROUP_ID + COMA + ROOM_ID + COMA
            + SUBJECT_ID + VALUES_3_QMARK;
    private static final String GET_ALL_LESSONS = SELECT + ASTERISK + FROM + TABLE_LESSONS;
    private static final String FIND_LESSON_BY_ID = SELECT + ASTERISK + FROM + TABLE_LESSONS + WHERE + ID + EQUALS_M
            + Q_MARK;
    private static final String FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS = SELECT + ASTERISK + FROM + TABLE_LESSONS + WHERE
            + GROUP_ID + EQUALS_M + Q_MARK + AND + ROOM_ID + EQUALS_M + Q_MARK + AND + SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String UPDATE_LESSON = UPDATE + TABLE_LESSONS + SET + GROUP_ID + EQUALS_M + Q_MARK + COMA
            + ROOM_ID + EQUALS_M + Q_MARK + COMA + SUBJECT_ID + EQUALS_M + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_LESSON_BY_ID = DELETE + FROM + TABLE_LESSONS + WHERE + ID + EQUALS_M + Q_MARK;

    private LessonMapper lessonMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonDAOImpl(DataSource dataSource, LessonMapper lessonMapper) {
        this.lessonMapper = lessonMapper;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        String lessonMessage = String.format("Lesson with groupId = %d, roomId = %d, subjectId = %d", groupId, roomId,
                subjectId);
        LOGGER.debug("Adding {}", lessonMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_LESSON, groupId, roomId, subjectId) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding {}", lessonMessage);
            } else {
                throw new DataOperationException("Couldn't add " + lessonMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = ("Failed to add " + lessonMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public Lesson getLessonById(long id) {
        LOGGER.debug("Getting Lesson by id '{}'", id);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(FIND_LESSON_BY_ID, new Object[] { id }, lessonMapper);
            LOGGER.info("Successfully retrived Lesson {}", lesson);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get Lesson by Id %d", id);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return lesson;
    }

    @Override
    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId) {
        String lessonMessage = String.format("Lesson by groupId = %d, roomId = %d, subjectId = %d", groupId, roomId,
                subjectId);
        LOGGER.debug("Getting {}", lessonMessage);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS,
                    new Object[] { groupId, roomId, subjectId }, lessonMapper);
            LOGGER.info("Successfully retrived {}", lessonMessage);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Failed to get %s", lessonMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return lesson;
    }

    @Override
    public List<Lesson> getAllLessons() {
        LOGGER.debug("Getting all lessons");
        List<Lesson> allLessons = new ArrayList<>();
        try {
            allLessons.addAll(jdbcTemplate.query(GET_ALL_LESSONS, lessonMapper));
            LOGGER.info("Successfully query for all lessons");
        } catch (DataAccessException e) {
            String failMessage = "Fail to get all lessons from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allLessons;
    }

    @Override
    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
        String lessonMessage = String.format("Lesson with ID = %d and groupId = %d, roomId = %d, subjectId = %d",
                lessonId, groupId, roomId, subjectId);
        LOGGER.debug("Updating {}", lessonMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(UPDATE_LESSON, groupId, roomId, subjectId, lessonId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated {}", lessonMessage);
            } else {
                throw new DataOperationException("Couldn't update " + lessonMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to update %s", lessonMessage);
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return actionResult;
    }

    @Override
    public boolean deleteLessonById(long id) {
        String lessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug("Deleting {}",lessonMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(DELETE_LESSON_BY_ID, id) > 0;
            if (actionResult) {
                LOGGER.info("Successful deleting {}", lessonMessage);
            } else {
                throw new DataOperationException("Couldn't delete " + lessonMessage);
            }
        } catch (DataAccessException e) {
            String failDeleteMessage = "Failed to delete " + lessonMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
        return actionResult;
    }

}
