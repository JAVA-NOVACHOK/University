package ua.com.nikiforov.services.timetables;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.mappers_dto.TimetableMapperDTO;

public abstract class PersonalTimetable {

    @Autowired
    private TimetableMapperDTO timetableMapper;

    public abstract List<DayTimetable>  getDayTimetable(String date, long personId);
    
    public abstract List<DayTimetable>  getMonthTimetable(String stringDate, long studentId);

    public TimetableMapperDTO getTimetableMapper() {
        return timetableMapper;
    }

    public List<DayTimetable> createMonthTimetable(List<TimetableDTO> allTimetablesList){
        List<DayTimetable> monthTimetable = new ArrayList<>();
        if (!allTimetablesList.isEmpty()) {
            for (int i = 1; i <= allTimetablesList.size(); i++) {
                DayTimetable dayTimetable = new DayTimetable();
                TimetableDTO previousTimetable = allTimetablesList.get(i - 1);
                dayTimetable.addTimetable(previousTimetable);
                dayTimetable.setDateInfo(parseInstantToDateInfo(previousTimetable));
                monthTimetable.add(dayTimetable);
                while (i < allTimetablesList.size()) {
                    TimetableDTO currentTimetable = allTimetablesList.get(i);
                    if (previousTimetable.getLessonDate().equals(currentTimetable.getLessonDate())) {
                        dayTimetable.addTimetable(currentTimetable);
                        i++;
                    } else {
                        Collections.sort(dayTimetable.getTimetables(), new CompareByPeriod());
                        break;
                    }
                }
            }
        }
        return monthTimetable;
    }
    
    public static DateInfo parseInstantToDateInfo( TimetableDTO timetable) {
        LocalDate date = timetable.getLessonDate();
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        String weekDay = zonedDateTime.getDayOfWeek().name();
        int monthDay = zonedDateTime.getDayOfMonth();
        String month = zonedDateTime.getMonth().toString();
        int year = zonedDateTime.getYear();
        return new DateInfo(weekDay, monthDay, month, year);
    }
    
    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

}
