package ua.com.nikiforov.services.lesson;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.lesson.LessonDAO;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.lesson.LessonInfo;
import ua.com.nikiforov.models.timetable.DateInfo;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        return lessonDAO.addLesson(groupId, roomId, subjectId);
    }

    @Override
    public Lesson getLessonById(long id) {
        return lessonDAO.getLessonById(id);
    }
    
    @Override
    public LessonInfo getLessonInfoById(long lessonId) {
        Lesson lesson = getLessonById(lessonId);
        return lessonDAO.getLessonInfoById(lesson);
    }

    public Lesson getLessonByGroupRoomSubjectIds(long groupId, int roomId, int subjectId) {
        return lessonDAO.getLessonByGroupRoomSubjectIds(groupId, roomId, subjectId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDAO.getAllLessons();
    }

    @Override
    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
        return lessonDAO.updateLesson(groupId, roomId, subjectId, lessonId);
    }

    @Override
    public boolean deleteLessonById(long id) {
        return lessonDAO.deleteLessonById(id);
    }

    @Override
    public DateInfo parseInstantToDateInfo(Timetable timetable, String zone) {
        Instant instant = timetable.getTime();
        ZonedDateTime zonedDateTime = getZonedDateTime(instant, zone);
        String weekDay = zonedDateTime.getDayOfWeek().name();
        int monthDay = zonedDateTime.getDayOfMonth();
        String month = zonedDateTime.getMonth().toString();
        int year = zonedDateTime.getYear();
        return new DateInfo(weekDay, monthDay, month, year);
    }
    
    private ZonedDateTime getZonedDateTime(Instant instant, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

}
