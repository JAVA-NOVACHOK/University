package ua.com.nikiforov.dao.lesson;

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
import ua.com.nikiforov.mappers.LessonInfoMapper;
import ua.com.nikiforov.mappers.LessonMapper;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.lesson.LessonInfo;

@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAOImpl.class);

    private static final String ADD_LESSON = "INSERT INTO lessons (group_id,subject_id,room_id) VALUES(?,?,?)";
    private static final String GET_ALL_LESSONS = "SELECT  *  FROM lessons ";
    private static final String FIND_LESSON_BY_ID = "SELECT  *  FROM lessons  WHERE lessons.lesson_id =  ? ";
    private static final String FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS = "SELECT * FROM lessons WHERE lessons.group_id =  ?  AND lessons.room_id = ? AND lessons.subject_id = ?";
    private static final String UPDATE_LESSON = "UPDATE lessons SET group_id = ?,room_id = ?,subject_id = ? WHERE lesson_id = ?";
    private static final String DELETE_LESSON_BY_ID = "DELETE  FROM lessons  WHERE lesson_id =  ? ";
    private static final String GET_SUBJECT_GROUP_ROOM_DATA = "SELECT subject_id, subject_name, room_id, room_number, seat_number, group_id, group_name FROM subjects, groups, rooms WHERE subject_id = ? AND room_id = ? AND group_id = ?";

    private JdbcTemplate jdbcTemplate;
    private LessonMapper lessonMapper;
    private LessonInfoMapper lessonInfoMapper;

    @Autowired
    public LessonDAOImpl(LessonMapper lessonMapper, LessonInfoMapper lessonInfoMapper, DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.lessonMapper = lessonMapper;
        this.lessonInfoMapper = lessonInfoMapper;
    }

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        String lessonMessage = String.format("Lesson with groupId = %d, roomId = %d, subjectId = %d", groupId,
                subjectId, roomId);
        LOGGER.debug("Adding {}", lessonMessage);
        boolean actionResult = false;
        try {
            actionResult = jdbcTemplate.update(ADD_LESSON, groupId, subjectId, roomId) > 0;
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
        String getlessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug("Getting '{}'", getlessonMessage);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(FIND_LESSON_BY_ID, new Object[] { id }, lessonMapper);
            LOGGER.info("Successfully retrived Lesson {}", lesson);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = String.format("Couldn't get %s", getlessonMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return lesson;
    }

    @Override
    public LessonInfo getLessonInfoById(Lesson lesson) {
        String lessonInfoMSG = String.format("LessonInfo by lesson with such data: %s", lesson);
        LOGGER.debug("Getting '{}'", lessonInfoMSG);
        LessonInfo lessonInfo;
        try {
            lessonInfo = jdbcTemplate.queryForObject(GET_SUBJECT_GROUP_ROOM_DATA,
                    new Object[] { lesson.getSubjectId(), lesson.getRoomId(), lesson.getGroupId() }, lessonInfoMapper);
            LOGGER.info("Successfully retrived LessonInfo {}", lessonInfo);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = "Couldn't get " + lessonInfoMSG;
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return lessonInfo;
    }

    @Override
    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int subjectId, int roomId) {
        String lessonMessage = String.format("Lesson by groupId = %d, roomId = %d, subjectId = %d", groupId, roomId,
                subjectId);
        LOGGER.debug("Getting {}", lessonMessage);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS,
                    new Object[] { groupId, subjectId, roomId }, lessonMapper);
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
        LOGGER.debug("Deleting {}", lessonMessage);
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
