package ua.com.nikiforov.services.timetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.TimetableDTO;
import ua.com.nikiforov.dao.timetables.TimetableDAO;

@Service
public class StudentTimetableService extends PersonalTimetable {

    private TimetableDAO studentsTimetable;

    @Autowired
   public StudentTimetableService(@Qualifier("studentTimetable") TimetableDAO studentsTimetable) {
        this.studentsTimetable = studentsTimetable;
    }

    
    @Override
    public List<DayTimetable>  getDayTimetable(String date, long groupId) {
        List<TimetableDTO> allTimetablesList = getTimetableDTOs(studentsTimetable.getDayTimetable(date, groupId));
        return createMonthTimetable(allTimetablesList);
    }

    @Override
    public List<DayTimetable> getMonthTimetable(String date, long groupId) {
        List<TimetableDTO> allTimetablesList = getTimetableDTOs(studentsTimetable.getMonthTimetable(date, groupId));
        return createMonthTimetable(allTimetablesList);
    }





}
