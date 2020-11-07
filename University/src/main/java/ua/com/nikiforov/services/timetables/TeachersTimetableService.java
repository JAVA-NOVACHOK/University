package ua.com.nikiforov.services.timetables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.timetables.TeachersTimetableDAO;
import ua.com.nikiforov.models.timetable.Timetable;

@Service
public class TeachersTimetableService extends PersonalTimetable {

    private Logger logger = LoggerFactory.getLogger(TeachersTimetableService.class);

    private TeachersTimetableDAO teachersTimetableDAO;

    @Autowired
    public TeachersTimetableService(TeachersTimetableDAO teachersTimetableDAO) {
        this.teachersTimetableDAO = teachersTimetableDAO;
    }

    @Override
    public List<Timetable> getDayTimetable(String date, long teacherId) {
        return teachersTimetableDAO.getDayTeacherTimetable(date, teacherId);
    }

    @Override
    public List<DayTimetable> getMonthTimetable(String date, long teacherId) {
        List<Timetable> allTimetablesList = teachersTimetableDAO.getMonthTeacherTimetable(date, teacherId);
        List<DayTimetable> monthTimetable = new ArrayList<>();
        if (!allTimetablesList.isEmpty()) {
            for (int i = 1; i <= allTimetablesList.size(); i++) {
                DayTimetable dayTimetable = new DayTimetable();
                Timetable previousTimetable = allTimetablesList.get(i - 1);
                dayTimetable.addTimetable(previousTimetable);
                dayTimetable.setDateInfo(parseInstantToDateInfo(previousTimetable));
                monthTimetable.add(dayTimetable);
                while (i < allTimetablesList.size()) {
                    Timetable currentTimetable = allTimetablesList.get(i);

                    if (previousTimetable.getTime().equals(currentTimetable.getTime())) {
                        dayTimetable.addTimetable(currentTimetable);
                        i++;
                    } else {
                        Collections.sort(dayTimetable.getTimetables(), new CompareByPeriod());
                        logger.debug("In else dayTimetable table size is = {}", dayTimetable.getTimetables().size());
                        break;
                    }

                }
            }
        }
        return monthTimetable;
    }

}
