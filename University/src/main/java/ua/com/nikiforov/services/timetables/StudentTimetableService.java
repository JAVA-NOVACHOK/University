package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.lesson.LessonService;

@Service
public class StudentTimetableService extends PersonalTimetable {

    private LessonService lessonService;

    @Autowired
   public StudentTimetableService(LessonService timetableDAO) {
        this.lessonService = timetableDAO;
    }

    
    @Override
    public List<Timetable> getDayTimetable(String date, long studentId) {
        return null;
    }

    @Override
    public List<DayTimetable> getMonthTimetable(String date, long studentId) {
        return null;
    }

   
}
