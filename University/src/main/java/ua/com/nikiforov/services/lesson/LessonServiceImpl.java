package ua.com.nikiforov.services.lesson;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.repositories.lesson.LessonRepository;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.mappers_dto.LessonMapperDTO;
import ua.com.nikiforov.models.lesson.Lesson;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class LessonServiceImpl implements LessonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServiceImpl.class);

    private static final String GETTING_MSG = "Getting '{}'";

    private LessonRepository lessonRepository;
    private LessonMapperDTO lessonMapper;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapperDTO lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public void addLesson(LessonDTO lessonDTO) {
        LOGGER.debug("Adding {}", lessonDTO);
        try {
            lessonRepository.save(lessonMapper.lessonDTOToLesson(lessonDTO));
            LOGGER.info("Successfully added {}", lessonDTO);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + lessonDTO, e);
        }
    }

    @Override
    @Transactional
    public LessonDTO getLessonById(long id) {
        String getLessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug(GETTING_MSG, getLessonMessage);
        LessonDTO lesson = lessonMapper.lessonToLessonDTO(lessonRepository.getOne(id));
        if (lesson == null) {
            String failGetByIdMessage = String.format("Couldn't get %s", getLessonMessage);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage);
        }
        LOGGER.info("Successfully retrieved Lesson {}", lesson);
        return lesson;
    }

    @Override
    public LessonDTO getLessonByAllArgs(LessonDTO lessonDTO) {
        Lesson lesson = lessonMapper.lessonDTOToLesson(lessonDTO);
        LOGGER.debug("Getting {}", lesson);
        LessonDTO lessonNew;
        try {
            lessonNew = lessonMapper.lessonToLessonDTO(lessonRepository.getLessonByAllArgs(lesson.getPeriod(),
                    lesson.getSubject(), lesson.getRoom(), lesson.getGroup(),
                    lesson.getLessonDate(), lesson.getTeacher()));
        } catch (NoResultException e) {
            String failGetByIdMessage = String.format("Couldn't get %s", lesson);
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage);
        }
        LOGGER.info("Successfully retrieved Lesson {}", lessonNew);
        return lessonNew;
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        LOGGER.debug(GETTING_MSG, "all lessons");
        List<LessonDTO> allLessons = new ArrayList<>();
        try {
            allLessons.addAll(lessonMapper.getLessonDTOList(lessonRepository.findAll()));
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
        try {
            lessonRepository.save(lesson);
            LOGGER.info("Successfully updated {}", lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Already exists " + lesson, e);
        }
    }

    @Override
    @Transactional
    public void deleteLessonById(long id) {
        String lessonMessage = String.format("Lesson by id %d", id);
        LOGGER.debug("Deleting {}", lessonMessage);
        try {
            lessonRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            String failDeleteMessage = "Failed to delete " + lessonMessage;
            LOGGER.error(failDeleteMessage);
            throw new DataOperationException(failDeleteMessage, e);
        }
    }

}
