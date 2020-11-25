package ua.com.nikiforov.dao.lesson;

import java.sql.Timestamp;
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
import ua.com.nikiforov.mappers.LessonMapper;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAOImpl.class);
//    int period, int subjectId, int roomId, long groupId, String date, long teacherId
    private static final String ADD_LESSON = "INSERT INTO lessons (period,subject_id,room_id,group_id,time,teacher_id) VALUES(?,?,?,?,?,?)";
    private static final String GET_ALL_LESSONS = "SELECT * FROM lessons ";
    private static final String FIND_LESSON_BY_ID = "SELECT * FROM lessons WHERE lessons.lesson_id = ? ";
    private static final String FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS = "SELECT * FROM lessons WHERE period = ? AND subject_id = ? AND room_id = ?  AND group_id = ? AND time = ? AND teacher_id = ?";
    private static final String UPDATE_LESSON = "UPDATE lessons SET period = ?, group_id = ?, subject_id = ?, room_id = ?, time = ?, teacher_id = ? WHERE lesson_id = ?";
    private static final String DELETE_LESSON_BY_ID = "DELETE FROM lessons WHERE lesson_id = ? ";

    private JdbcTemplate jdbcTemplate;
    private LessonMapper lessonMapper;

    @Autowired
    public LessonDAOImpl(LessonMapper lessonMapper, DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.lessonMapper = lessonMapper;
    }

    @Override
    public boolean addLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        String lessonMessage = String.format(
                "Lesson with period = %d, subjectId = %d, roomId = %d, groupId = %d, date = %s, teacherId = %d",
                period, subjectId, roomId, groupId, date, teacherId);
        LOGGER.debug("Adding {}", lessonMessage);
        boolean actionResult = false;
        Timestamp time = getTimestampFromString(date);
        try {
            actionResult = jdbcTemplate.update(ADD_LESSON, period, subjectId, roomId, groupId, time,
                    teacherId) > 0;
            if (actionResult) {
                LOGGER.info("Successful adding {}", lessonMessage);
            } else {
                throw new DataOperationException("Couldn't add " + lessonMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = ("Failed to add " + lessonMessage);
            LOGGER.error(failMessage + e);
            e.printStackTrace();
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
    public Lesson getLessonByAllArgs(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        String lessonMessage = String.format("Lesson by subjectId = %d, roomId = %d, groupId = %d  ", subjectId, roomId,
                groupId);
        LOGGER.debug("Getting {}", lessonMessage);
        Timestamp time = PersonalTimetable.getTimestampFromString(date);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS,
                    new Object[] {period, subjectId, roomId, groupId, time, teacherId}, lessonMapper);
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
    public boolean updateLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId,
            long lessonId) {
        String lessonMessage = String.format(
                "Lesson with ID = %d and period = %d, subjectId = %d, roomId = %d, groupId = %d, date = %s, teacherId = %d",
                lessonId, period, subjectId, roomId, groupId, date, teacherId);
        LOGGER.debug("Updating {}", lessonMessage);
        boolean actionResult = false;
        Timestamp time = getTimestampFromString(date);
        try {
            actionResult = jdbcTemplate.update(UPDATE_LESSON, period,groupId, subjectId, roomId,  time,
                    teacherId, lessonId) > 0;
            if (actionResult) {
                LOGGER.info("Successfully updated {}", lessonMessage);
            } else {
                throw new DataOperationException("Couldn't update " + lessonMessage);
            }
        } catch (DataAccessException e) {
            String failMessage = String.format("Failed to update %s", lessonMessage);
            e.printStackTrace();
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

    private Timestamp getTimestampFromString(String stringDate) {
//        return Timestamp.valueOf(stringDate + " 12:00:00");
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }

}
