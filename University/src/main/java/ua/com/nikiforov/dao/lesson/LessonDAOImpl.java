package ua.com.nikiforov.dao.lesson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers_dto.LessonMapperDTO;
import ua.com.nikiforov.models.lesson.Lesson;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAOImpl.class);

    private static final String GET_ALL_LESSONS = "SELECT l FROM Lesson l";
    private static final String FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS = "SELECT l FROM Lesson l WHERE l.period = ?1 " +
            "AND l.subject = ?2 AND l.room = ?3  AND l.group = ?4 AND l.lessonDate = ?5 AND l.teacher = ?6";
    private static final String DELETE_LESSON_BY_ID = "DELETE FROM Lesson l WHERE l.id = ?1";

    private static final int FIRST_PARAMETER_INDEX = 1;
    private static final int SECOND_PARAMETER_INDEX = 2;
    private static final int THIRD_PARAMETER_INDEX = 3;
    private static final int FOURTH_PARAMETER_INDEX = 4;
    private static final int FIFTH_PARAMETER_INDEX = 5;
    private static final int SIXTH_PARAMETER_INDEX = 6;

    private LessonMapperDTO lessonMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LessonDAOImpl(LessonMapperDTO lessonMapper) {
        this.lessonMapper = lessonMapper;
    }

    @Override
    @Transactional
    public void addLesson(LessonDTO lesson) {
        LOGGER.debug("Adding {}", lesson);
        entityManager.merge(lessonMapper.lessonDTOToLesson(lesson));
        LOGGER.info("Successful adding {}", lesson);
    }

    @Override
    @Transactional
    public Lesson getLessonById(long id) {
        String getLessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug("Getting '{}'", getLessonMessage);
        Lesson lesson = entityManager.find(Lesson.class, id);
        if (lesson == null) {
            String failGetByIdMessage = String.format("Couldn't get %s", getLessonMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage);
        }
        LOGGER.info("Successfully retrieved Lesson {}", lesson);
        return lesson;
    }

    @Override
    public Lesson getLessonByAllArgs(LessonDTO lessonDTO) {
        Lesson lesson = lessonMapper.lessonDTOToLesson(lessonDTO);
        LOGGER.debug("Getting {}", lesson);
        Lesson lessonNew;
        try {
            lessonNew = (Lesson) entityManager.createQuery(FIND_LESSON_BY_GROUP_ROOM_SUBJECT_IDS)
                    .setParameter(FIRST_PARAMETER_INDEX, lesson.getPeriod())
                    .setParameter(SECOND_PARAMETER_INDEX, lesson.getSubject())
                    .setParameter(THIRD_PARAMETER_INDEX, lesson.getRoom())
                    .setParameter(FOURTH_PARAMETER_INDEX, lesson.getGroup())
                    .setParameter(FIFTH_PARAMETER_INDEX, lesson.getLessonDate())
                    .setParameter(SIXTH_PARAMETER_INDEX, lesson.getTeacher())
                    .getSingleResult();
        }catch (NoResultException e){
            String failGetByIdMessage = String.format("Couldn't get %s", lesson);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage);
        }
        LOGGER.info("Successfully retrieved Lesson {}", lessonNew);
        return lessonNew;
    }

    @Override
    public List<Lesson> getAllLessons() {
        LOGGER.debug("Getting all lessons");
        List<Lesson> allLessons = new ArrayList<>();
        try {
            allLessons.addAll(entityManager.createQuery(GET_ALL_LESSONS, Lesson.class).getResultList());
            LOGGER.info("Successfully query for all lessons");
        } catch (PersistenceException e) {
            String failMessage = "Fail to get all lessons from DB.";
            LOGGER.error(failMessage);
            throw new DataOperationException(failMessage, e);
        }
        return allLessons;
    }

    @Override
    @Transactional
    public void updateLesson(LessonDTO lessonDTO) {
        Lesson lesson = lessonMapper.lessonDTOToLesson(lessonDTO);
        LOGGER.debug("Updating {}", lesson);
        entityManager.merge(lesson);
        LOGGER.info("Successful updated {}", lesson);
    }

    @Override
    @Transactional
    public void deleteLessonById(long id) {
        String lessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug("Deleting {}", lessonMessage);
        try {
            entityManager.createQuery(DELETE_LESSON_BY_ID)
                    .setParameter(FIRST_PARAMETER_INDEX, id)
                    .executeUpdate();
        } catch (PersistenceException e) {
            String failDeleteMessage = "Failed to delete " + lessonMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }
}
