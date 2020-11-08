package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.TimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.lesson.LessonService;

@Service
public class StudentTimetableService extends PersonalTimetable {

    private TimetableDAO studentsTimetable;

    @Autowired
   public StudentTimetableService(@Qualifier("studentTimetable") TimetableDAO studentsTimetable) {
        this.studentsTimetable = studentsTimetable;
    }

    
    @Override
    public List<Timetable> getDayTimetable(String date, long groupId) {
        return studentsTimetable.getDayTimetable(date, groupId);
    }

    @Override
    public List<DayTimetable> getMonthTimetable(String date, long groupId) {
        List<Timetable> allTimetablesList = studentsTimetable.getMonthTimetable(date, groupId);
        return createMonthTimetable(allTimetablesList);
    }

   
}
