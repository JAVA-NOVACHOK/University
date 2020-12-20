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
            lessonDAO.addLesson(getLesson(lesson));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate lesson while adding!", e);
        }
    }

    @Override
    public LessonDTO getLessonById(long id) {
        return getLessonDTO(lessonDAO.getLessonById(id));
    }

    public LessonDTO getLessonByAllArgs(LessonDTO lesson) {
        return getLessonDTO(lessonDAO.getLessonByAllArgs(getLesson(lesson)));
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
            lessonDAO.updateLesson(getLesson(lesson));
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
        long groupId = lesson.getGroupId();
        int subjectId = lesson.getSubjectId();
        int roomId = lesson.getRoomId();
        LocalDate time = lesson.getTime();
        int period = lesson.getPeriod();
        long teacherId = lesson.getTeacherId();
        return new LessonDTO(id, period, groupId, subjectId, roomId, time, teacherId);
    }

    private Lesson getLesson(LessonDTO lesson) {
        long lessonId = lesson.getId();
        int period = lesson.getPeriod();
        int subjectId = lesson.getSubjectId();
        int roomId = lesson.getRoomId();
        long groupId = lesson.getGroupId();
        String date = lesson.getDate();
        long teacherId = lesson.getTeacherId();
        LocalDate time = getLocalDate(date);
        return new Lesson(lessonId, period, groupId, subjectId, roomId, time, teacherId);
    }

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

}
