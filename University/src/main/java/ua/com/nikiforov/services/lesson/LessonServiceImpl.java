package ua.com.nikiforov.services.lesson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.models.lesson.Lesson;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public boolean addLesson(LessonDTO lesson) {
        return lessonDAO.addLesson(lesson);
    }

    @Override
    public LessonDTO getLessonById(long id) {
        return getLessonDTO(lessonDAO.getLessonById(id));
    }

    public LessonDTO getLessonByAllArgs(int period, int subjectId, int roomId, long groupId, String date,
            long teacherId) {
        return getLessonDTO(lessonDAO.getLessonByAllArgs(period, subjectId, roomId, groupId, date, teacherId));
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
    public boolean updateLesson(LessonDTO lesson) {
        return lessonDAO.updateLesson(lesson);
    }

    @Override
    public boolean deleteLessonById(long id) {
        return lessonDAO.deleteLessonById(id);
    }

    public LessonDTO getLessonDTO(Lesson lesson) {
        long id = lesson.getId();
        long groupId = lesson.getGroupId();
        int subjectId = lesson.getSubjectId();
        int roomId = lesson.getRoomId();
        LocalDate time = lesson.getTime();
        int period = lesson.getPeriod();
        long teacherId = lesson.getTeacherId();
        String date = lesson.getDate();
        return new LessonDTO(id, period, groupId, subjectId, roomId, time, teacherId, date);
    }

}
