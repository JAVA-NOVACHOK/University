package ua.com.nikiforov.services.lesson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.mappers_dto.LessonMapperDTO;

@Service
public class LessonServiceImpl implements LessonService {

    private LessonDAO lessonDAO;
    private LessonMapperDTO lessonMapper;

    @Autowired
    public LessonServiceImpl(LessonDAO lessonDAO,LessonMapperDTO lessonMapper) {
        this.lessonDAO = lessonDAO;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public void addLesson(LessonDTO lesson) {
        try {
            lessonDAO.addLesson(lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate lesson while adding!", e);
        }
    }

    @Override
    public LessonDTO getLessonById(long id) {
        return lessonMapper.lessonToLessonDTO(lessonDAO.getLessonById(id));
    }

    public LessonDTO getLessonByAllArgs(LessonDTO lesson) {
        return lessonMapper.lessonToLessonDTO(lessonDAO.getLessonByAllArgs(lesson));
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        return lessonMapper.getLessonDTOList(lessonDAO.getAllLessons());
    }

    @Override
    public void updateLesson(LessonDTO lesson) {
        try {
            lessonDAO.updateLesson(lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate lesson while editing!", e);
        }
    }

    @Override
    public void deleteLessonById(long id) {
        lessonDAO.deleteLessonById(id);
    }

}
