package ua.com.nikiforov.services.lesson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.models.lesson.Lesson;

@Service
public class LessonServiceImpl implements LessonService {

    private LessonDAO lessonDAO;

    @Autowired
    public LessonServiceImpl(LessonDAO lessonDAO) {
        this.lessonDAO = lessonDAO;
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
        return getLessonDTO(lessonDAO.getLessonById(id));
    }

    public LessonDTO getLessonByAllArgs(LessonDTO lesson) {
        return getLessonDTO(lessonDAO.getLessonByAllArgs(lesson));
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonDAO.getAllLessons();
        List<LessonDTO> lessonsDTO = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonsDTO.add(getLessonDTO(lesson));
        }
        return lessonsDTO;
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

    public LessonDTO getLessonDTO(Lesson lesson) {
        long id = lesson.getId();
        long groupId = lesson.getGroup().getGroupId();
        int subjectId = lesson.getSubject().getId();
        int roomId = lesson.getRoom().getId();
        LocalDate time = lesson.getLessonDate();
        int period = lesson.getPeriod();
        long teacherId = lesson.getTeacher().getId();
        return new LessonDTO(id, period, groupId, subjectId, roomId, time, teacherId);
    }

}
